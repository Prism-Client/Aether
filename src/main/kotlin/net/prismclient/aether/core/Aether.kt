package net.prismclient.aether.core

import net.prismclient.aether.core.event.*
import net.prismclient.aether.core.event.UIEventBus.publish
import net.prismclient.aether.core.input.MouseButtonType
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.property.Focusable
import net.prismclient.aether.core.util.shorthands.RGBA
import net.prismclient.aether.core.util.shorthands.notNull
import net.prismclient.aether.core.util.shorthands.rel
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.disableOptimizations
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.screen.CloseableScreen
import net.prismclient.aether.ui.screen.UIScreen
import java.util.*

// TODO: Event plotting optimizations (invoke only when the composition is within range)

/**
 * [Aether]
 *
 * @author sen
 * @since 1.0
 */
open class Aether(renderer: UIRenderer) {
    var activeScreen: UIScreen? = null
    var focusedComponent: Focusable? = null
    var displayWidth: Float = 0f
        private set
    var displayHeight: Float = 0f
        protected set
    var devicePixelRatio: Float = 0f
        protected set
    var mouseX: Float = 0f
        protected set
    open var mouseY: Float = 0f
        protected set

    open var rasterizationQueue: LinkedList<Composition>? = null

    /**
     * The compositions represented as a HashMap with the key as the name of the composition.
     */
    open var compositions: ArrayList<Composition>? = null
        protected set

    /**
     * The default composition, where all "composition-less" composables are placed. The size
     * of the composition is equal to size of the window.
     */
    var defaultComposition: Composition? = null
        protected set

    init {
        instance = this
        Companion.renderer = renderer
    }

    /**
     * Invoked when the display is resized. The [devicePxRatio] is the ratio of the resolution in
     * actual pixels compared to the pixels of the window size, which is used for Retina displays.
     * 1f is considered the default value.
     */
    open fun update(displayWidth: Float, displayHeight: Float, devicePxRatio: Float) {
        // Update the state
        this.displayWidth = displayWidth
        this.displayHeight = displayHeight
        this.devicePixelRatio = devicePxRatio.coerceAtLeast(1f)

        if (activeScreen != null) {
            check()
            renderer.beginFrame(displayWidth, displayHeight, devicePxRatio)
            compositions!!.filter(Composition::isTopLayer).forEach(Composition::compose)
            renderer.cancelFrame()
        }
    }

    open fun renderFrames() {
        if (activeScreen.notNull()) {
            while (rasterizationQueue!!.isNotEmpty()) {
                rasterizationQueue!!.poll().rasterize()
            }
        }
    }

    open fun render() {
        publish(PreRenderEvent())
        if (activeScreen.notNull()) {
            renderer.beginFrame(displayWidth, displayHeight, devicePixelRatio)
            for (i in compositions!!.indices) {
                val composition = compositions!![i]
                if (composition.isTopLayer()) {
                    composition.render()
                }
            }
            renderer.endFrame()
        }
        publish(RenderEvent())
    }

    /**
     * Invoked when the mouse changes its state. This can be when it is pressed, released, or moved.
     *
     * @param mouseX The x position of the mouse relative to the top left corner of the screen.
     * @param mouseY The y position of the mouse relative to the top left corner of the screen.
     * @param mouseButton The button which was pressed
     * @param isRelease When true, the mouse button was released. The value does not matter if [mouseButton] is [MouseButtonType.None].
     * @see mouseButton
     */
    open fun mouseChanged(mouseX: Float, mouseY: Float, mouseButton: MouseButtonType, isRelease: Boolean) {
        if (!safeCheck()) throw RuntimeException("Attempted to change mouse, but the check failed.")

        this.mouseX = mouseX
        this.mouseY = mouseY

        if (mouseButton != MouseButtonType.None) {
            if (!isRelease) {
                compositions!!.forEach { composition ->
                    if (composition.mouseWithinBounds()) {
                        val item = findDeepest(mouseX, mouseY, composition)
                        if (item != null) {
                            val event = MousePress(mouseX, mouseY, mouseButton, item)
                            item.publish(event)
                            publish(event)
//                            composition.compose()
                            return
                        }
                    }
                }
            } else {
                publish(MouseRelease(mouseX, mouseY, mouseButton))
            }
        } else {
            publish(MouseMove(mouseX, mouseY))
        }
    }

    /**
     * Invoked when the mouse scroll or trackpad is interacted with.
     */
    open fun mouseScrolled(dstX: Float, dstY: Float) {
        for (i in compositions!!.size - 1 downTo 0) {
            val composition = compositions!![i]
            if (composition.mouseWithinBounds()) {
                val item = scrollFind(composition)

                if (item != null) {
                    focusedComponent = item

                    break
                }
            }
        }

        val composable = (focusedComponent as? Composable)

        composable?.publish(MouseScrolled(dstX, dstY, composable))
        composable?.recompose()
    }

    /**
     * Finds the deepest focusable within the mouse bounds
     */
    private fun scrollFind(group: ComposableGroup): Focusable? {
        for (composable in group.children.size - 1 downTo 0) {
            val item = group.children[composable]

            if (item.mouseWithinBounds()) {
                if (item is ComposableGroup) {
                    val output = scrollFind(item)
                    if (output != null) return output
                }

                if (item is Focusable && item.wantsFocus()) {
                    return item
                }
            }
        }
        return null
    }

    /**
     * Returns the deepest item within the [boundX] and [boundY] from the given [group].
     */
    private fun findDeepest(boundX: Float, boundY: Float, group: ComposableGroup): Composable? {
        for (composable in group.children.size - 1 downTo 0) {
            val item = group.children[composable]

            if (item.mouseWithinBounds()) {
                return if (item is ComposableGroup) {
                    findDeepest(boundX, boundY, item) ?: item
                } else {
                    item
                }
            }
        }
        return null
    }

    /**
     * Used internally for [Aether] to display a screen.
     *
     * @see Aether.Companion.displayScreen to display a screen
     */
    open fun screen(screen: UIScreen) {
        deleteActiveScreen()
        compositions = arrayListOf()
        compositions = arrayListOf()
        rasterizationQueue = LinkedList()
        activeScreen = screen
        defaultComposition = Composition("Default", CompositionModifier().size(1.rel, 1.rel).disableOptimizations())
        activeScreen!!.compose()
        update(displayWidth, displayHeight, devicePixelRatio)
    }

    open fun deleteActiveScreen() {
        if (activeScreen != null) {
            compositions!!.forEach(Composition::delete)
            compositions = null
            rasterizationQueue = null
            if (activeScreen!! is CloseableScreen)
                (activeScreen!! as CloseableScreen).closeScreen()
        }
    }

    /**
     * Returns true if the activeScreen and composition doesn't equal null
     */
    // TODO: Remove temp safeCheck
    open fun safeCheck(): Boolean = activeScreen != null && compositions != null

    open fun check() {
        if (activeScreen == null || compositions == null)
            throw RuntimeException("[Aether] -> Failed check because activeScreen or compositions was null.")
    }

    /**
     * The companion object of [Aether] which provides a few utility fields and functions.
     *
     */
    companion object {
        @JvmStatic
        lateinit var instance: Aether

        @JvmStatic
        lateinit var renderer: UIRenderer

        @JvmStatic
        fun displayScreen(screen: UIScreen) = instance.screen(screen)
    }
}