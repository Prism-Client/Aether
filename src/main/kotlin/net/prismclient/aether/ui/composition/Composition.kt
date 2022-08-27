package net.prismclient.aether.ui.composition

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.renderer.UIFramebuffer
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.or

// TODO: disable optimize composition
// TODO: Limit composition framerate
// TODO: nested compositions
// TODO: Remove the disableOptimization removing fbo
/**
 * A composition is a group of components which fills a portion or full area of the screen. Compositions allow
 * for Aether to streamline component layout calculations and cache frames to improve the overall speed from initial
 * creation to actual render times (frames) of your UI.
 *
 * @author sen
 * @since 1.0
 */
open class Composition(val name: String, modifier: CompositionModifier) : Composable(modifier), ComposableGroup {
    override val modifier: CompositionModifier get() = super.modifier as CompositionModifier
    override val children: ArrayList<Composable> = arrayListOf()

    override fun parentWidth(): Float = Aether.instance.displayWidth
    override fun parentHeight(): Float = Aether.instance.displayHeight

    override fun layoutXOffset(): Float = 0f
    override fun layoutYOffset(): Float = 0f

    /**
     * Returns the parent composition or this.
     */
    override var composition: Composition
        get() = compositionRef ?: this
        set(value) {
            compositionRef = value
        }

    /**
     * The FrameBuffer reference. This is automatically allocated when [CompositionModifier.optimizeComposition] is true.
     */
    open var framebuffer: UIFramebuffer? = null
        protected set

    // -- Core -- //

    override fun compose() {
        //if (!composed || dynamic) {
            modifier.preCompose(this)

            composeSize()
            composePosition()

            // Compose all static components
            children.filterNot(Composable::dynamic).forEach(Composable::compose)

            // Compose all dynamic components after the initial composition has been created.
//            if (dynamic)
//                children.filter(Composable::dynamic).forEach(Composable::compose)
            modifier.compose(this)
            rasterize()
//        }
    }

    override fun render() {
        renderer {
            if (modifier.optimizeComposition) {
                color(-1)
                path {
                    renderer.imagePattern(framebuffer!!.imagePattern, x, y, width, height, 0f, 1f)
                    rect(x, y, width, height, modifier.background?.backgroundRadius)
                }.fillPaint()
            } else {
                scissor(x, y, width, height) {
                    modifier.preRender()
                    children.forEach(Composable::render)
                    modifier.render()
                }
            }
        }
    }

    override fun recompose() {
        compose()
    }

    /**
     * Converts the active composition layout to a raster image.
     */
    open fun rasterize() {
        if (!modifier.optimizeComposition) return

//        println("Rasterized the composition")

        framebuffer = framebuffer ?: Aether.renderer.createFBO(width, height)

        UIRendererDSL.renderToFramebuffer(framebuffer!!) {
            // Translate the composition by the inverse position. This is done
            // because it is rendered to a framebuffer where the origin is now
            // (x, y) instead of 0, 0 as it is automatically set to (x, y) when
            // the image of the framebuffer is rendered.
            translate(-x, -y) {
                // UIRendererDSL saving is disabled to account for saving
                // the state within the translation call above.
                shouldSave = false
                modifier.preRender()
                children.forEach(Composable::render)
                modifier.render()
                shouldSave = true
            }
        }
    }

    // -- Util -- //

    /**
     * Returns true if this Composition is at the top of the Composition tree. A composition within
     * another composition would return false as it is not at the top.
     */
    open fun isTopLayer(): Boolean = composition == this // TODO: Update to fit Compositions within Compositions
}

/**
 * The [UIModifier] for [Composition]s. See the properties for more information
 *
 * @author sen
 * @since 1.0
 */
open class CompositionModifier : UIModifier<CompositionModifier>() {
    /**
     * True by default. The composition will use a framebuffer.
     *
     * @see frameRate
     */
    open var optimizeComposition: Boolean = true // TODO: Move to modifier

    /**
     * The frame rate of this composition. The frame is updated regardless of this property on an event, and does nothing
     * if [optimizeComposition] is false.
     *
     *      -1 = unlimited
     *       0 = disabled (default)
     *      >0 = frames per second
     *
     * @see optimizeComposition
     */
    open var frameRate: Int = 0 // TODO: Convert to class

    override fun copy(): CompositionModifier = CompositionModifier().also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.anchorPoint = anchorPoint?.copy()
        it.padding = padding?.copy()
        it.margin = margin?.copy()
        it.background = background?.copy()
        it.optimizeComposition = optimizeComposition
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
            optimizeComposition = other.optimizeComposition
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