package net.prismclient.aether.ui.composition

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.renderer.UIFramebuffer
import java.lang.RuntimeException

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
open class Composition(val name: String, modifier: CompositionModifier<*>) : Composable(modifier), ComposableGroup {
    override var composition: Composition? = this
    override val modifier: CompositionModifier<*> get() = super.modifier as CompositionModifier
    override val children: ArrayList<Composable> = arrayListOf()

    override fun parentWidth(): Float = if (isTopLayer()) Aether.instance.displayWidth else super.parentWidth()
    override fun parentHeight(): Float = if (isTopLayer()) Aether.instance.displayHeight else super.parentHeight()

    /**
     * The FrameBuffer reference. This is automatically allocated when [CompositionModifier.optimizeComposition] is true.
     */
    open var framebuffer: UIFramebuffer? = null
        protected set

    init {
        Aether.instance.compositions?.add(this)
    }

    // -- Core -- //

    override fun compose() {
        modifier.preCompose(this)
        children.forEach(Composable::compose)
        modifier.compose(this)
        requestRasterization()
    }

    override fun render() {
        Renderer {
            if (modifier.optimizeComposition) {
                color(-1)
                path {
                    renderer.imagePattern(framebuffer!!.imagePattern, x, y, width, height, 0f, 1f)
                    rect(x, y, width, height, modifier.background?.backgroundRadius)
                }.fillPaint()
            } else {
                renderer.save()
                if (modifier.clipContent) renderer.scissor(relX, relY, relWidth, relHeight)
                modifier.preRender()
                children.forEach(Composable::render)
                modifier.render()
                renderer.restore()
            }
        }
    }

    override fun recompose() {
        if (isTopLayer()) compose() else super.recompose()
    }

    open fun requestRasterization() {
        Aether.instance.rasterizationQueue?.add(this)
    }

    /**
     * Converts the active composition layout to a raster image.
     */
    open fun rasterize() {
        if (!modifier.optimizeComposition) return

        if (framebuffer == null || framebuffer!!.width != width || framebuffer!!.height != height) {
            if (framebuffer != null)
                Aether.renderer.deleteFBO(framebuffer!!)
            framebuffer = Aether.renderer.createFBO(width, height)
        }

        UIRendererDSL.renderToFramebuffer(framebuffer!!) {
            translate(-x, -y) {
                modifier.preRender()
                children.forEach(Composable::render)
                modifier.render()
            }
        }
    }


    // -- Util -- //

    /**
     * Deletes any allocated resources if necessary.
     */
    open fun delete() {
        if (framebuffer != null) {
            Aether.renderer.deleteFBO(framebuffer!!)
        }
    }


    /**
     * Returns true if this Composition is at the top of the Composition tree. A composition within
     * another composition would return false as it is not at the top.
     */
    open fun isTopLayer(): Boolean = composition == this
}

/**
 * The [UIModifier] for [Composition]s. See the properties for more information
 *
 * @author sen
 * @since 1.0
 *
 * @see DefaultCompositionModifier
 */
abstract class CompositionModifier<T : CompositionModifier<T>> : UIModifier<T>() {
    /**
     * True by default. The composition will use a framebuffer.
     *
     * @see frameRate
     */
    open var optimizeComposition: Boolean = true

    /**
     * Will clip any content that exceeds the bounds of this. The content will **always**
     * be clipped when [optimizeComposition] is true.
     */
    open var clipContent: Boolean = true
}

/**
 * Disables rendering to a framebuffer for this layout. Some layouts might not need optimizations
 * as they are not complex, or nested within another.
 */
fun <T : CompositionModifier<*>> T.disableOptimizations() = apply {
    optimizeComposition = false
}

fun CompositionModifier(): DefaultCompositionModifier = DefaultCompositionModifier()

/**
 * The default implementation of [CompositionModifier].
 *
 * @author sen
 * @since 1.0
 */
class DefaultCompositionModifier : CompositionModifier<DefaultCompositionModifier>() {
    override fun animate(
        context: AnimationContext<*>,
        initial: DefaultCompositionModifier?,
        start: DefaultCompositionModifier?,
        end: DefaultCompositionModifier?,
        progress: Float,
        completed: Boolean,
    ) {
        TODO("Not yet implemented")
    }

    override fun copy(): DefaultCompositionModifier = DefaultCompositionModifier().also {
        it.x = x.copy
        it.y = y.copy
        it.width = width.copy
        it.height = height.copy
        it.anchorPoint = anchorPoint.copy
        it.padding = padding.copy
        it.margin = margin.copy
        it.opacity = opacity.copy
        it.background = background.copy
        it.optimizeComposition = optimizeComposition
        it.clipContent = clipContent
    }

    override fun merge(other: DefaultCompositionModifier?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
            width = other.width or width
            height = other.height or height
            anchorPoint = other.anchorPoint or anchorPoint
            padding = other.padding or padding
            margin = other.margin or margin
            opacity = other.opacity or opacity
            background = other.background or background
            optimizeComposition = other.optimizeComposition
            clipContent = other.clipContent
        }
    }
}