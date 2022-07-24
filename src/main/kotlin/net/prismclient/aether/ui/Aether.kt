package net.prismclient.aether.ui

import net.prismclient.aether.ui.composition.UIComposition
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.other.UIMouseButton

/**
 * [Aether]
 *
 * @author sen
 * @since 1.0
 */
open class Aether(renderer: UIRenderer) {
    var activeScreen: UIScreen? = null

    var displayWidth: Float = 0f
        private set
    var displayHeight: Float = 0f
        protected set
    var devicePixelRatio: Float = 0f
        protected set

    /**
     * The compositions represented as a HashMap with the key as the name of the composition.
     */
    var compositions: HashMap<String, UIComposition>? = null
        protected set

    /**
     * The default composition, where all "composition-less" components are placed. The size
     * of the composition is equal to size of the window.
     */
    var defaultComposition: UIComposition? = null

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
        check()

        // Update the state
        this.displayWidth = displayWidth
        this.displayHeight = displayHeight
        this.devicePixelRatio = devicePxRatio.coerceAtLeast(1f)

        compositions!!.values.forEach(UIComposition::compose)
    }

    /**
     * Invoked when the mouse changes its state. This can be when it is pressed, released, or moved.
     *
     * @param mouseX The x position of the mouse relative to the top left corner of the screen.
     * @param mouseY The y position of the mouse relative to the top left corner of the screen.
     * @param mouseButton The button which was pressed
     * @param isRelease When true, the mouse button was released. The value does not matter if [mouseButton] is [UIMouseButton.None].
     * @see mouseButton
     */
    open fun mouseChanged(mouseX: Float, mouseY: Float, mouseButton: UIMouseButton, isRelease: Boolean) {

    }

    /**
     * Used internally for [Aether] to display a screen.
     *
     * @see Aether.Companion.displayScreen to display a screen
     */
    open fun screen(screen: UIScreen) {
        defaultComposition = createComposition("Default")
    }

    /**
     * Creates a new composition from the given [name].
     */
    open fun createComposition(name: String): UIComposition = UIComposition().also {
        check()
        compositions!![name] = it
    }

    open fun check() {
        if (activeScreen == null || compositions == null)
            throw RuntimeException("[Aether] -> Failed check because activeScreen or compositions was null.")
    }

    /**
     * The companion object of [Aether] which provides a few utility fields and functions.
     *
     */
    companion object {
        @JvmStatic lateinit var instance: Aether
        @JvmStatic lateinit var renderer: UIRenderer

        @JvmStatic
        fun displayScreen(screen: UIScreen) = instance.screen(screen)
    }
}