package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.renderer.UIFramebuffer
import net.prismclient.aether.ui.util.shorthands.or

// TODO: disable optimize composition
// TODO: Limit composition framerate
/**
 * A composition is a group of components which fills a portion or full area of the screen. Compositions allow
 * for Aether to streamline component layout calculations and cache frames to improve the overall speed from initial
 * creation to actual render times (frames) of your UI.
 *
 * @author sen
 * @since 1.0
 */
open class Composition(val name: String, modifier: CompositionModifier) : Composable(modifier) {
    override val modifier: CompositionModifier get() = super.modifier as CompositionModifier

    open val components: ArrayList<UIComponent<*>> = arrayListOf()
    open var framebuffer: UIFramebuffer? = null
        protected set

    /**
     * True by default. The composition will use a framebuffer.
     */
    var optimizeComposition: Boolean = true // TODO: Move to modifier

    // -- Core -- //

    override fun compose() {
        if (!composed || dynamic) {
            modifier.preUpdate(this)

            updateSize()
            updateAnchor()
            updatePosition()

            // Compose all static components
            components.filterNot(UIComponent<*>::dynamic).forEach(UIComponent<*>::compose)

            // Compose all dynamic components after the initial composition has been created.
            if (dynamic)
                components.filter(UIComponent<*>::dynamic).forEach(UIComponent<*>::compose)
            modifier.update(this)
            rasterize()
        }
    }

    override fun render() {
        renderer {
            if (optimizeComposition) {
                color(-1)
                path {
                    imagePattern(framebuffer!!.imagePattern, x, y, width, height, 0f, 1f)
                    rect(x, y, width, height, modifier.background?.backgroundRadius)
                }.fillPaint()
            }
        }
    }


    /**
     * Converts the active composition layout to a raster image.
     */
    open fun rasterize() {
        if (!optimizeComposition) return

        framebuffer = framebuffer ?: Aether.renderer.createFBO(width, height)

        UIRendererDSL.renderToFramebuffer(framebuffer!!) {
            modifier.preRender()
            components.forEach(UIComponent<*>::render)
            modifier.render()
        }
    }

    // -- Shorthands -- //

}

/**
 * The [UIModifier] for [Composition]s. See the properties for more information
 *
 * @author sen
 * @since 1.0
 */
open class CompositionModifier : UIModifier<CompositionModifier>() {
    /**
     * The frame rate of this composition. The frame is updated regardless of this property on an event.
     *
     *      -1 = unlimited
     *       0 = disabled (default)
     *      >0 = frames per second
     */
    var frameRate: Int = 0 // TODO: Convert to class

    override fun copy(): CompositionModifier = CompositionModifier().also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.anchorPoint = anchorPoint?.copy()
        it.padding = padding?.copy()
        it.margin = margin?.copy()
        it.background = background?.copy()
        it.frameRate = frameRate
    }

    override fun merge(other: CompositionModifier?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
            width = other.width or width
            height = other.height or height
            anchorPoint = other.anchorPoint or anchorPoint
            padding = other.padding or padding
            margin = other.margin or margin
            background = other.background or background
            frameRate = other.frameRate
        }
    }

    override fun animate(start: CompositionModifier?, end: CompositionModifier?, fraction: Float) {
        TODO("Animate CompositionModifier")
    }
}

/**
 * Limits the frame rate of the composition of this [CompositionModifier].
 *
 * @see [CompositionModifier.frameRate]
 */
fun Composition.limitFrameRate(frameRate: Int) = apply {
    modifier.frameRate = frameRate
}