package net.prismclient.aether.ui.composition

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.color.UIAlpha
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.renderer.UIFramebuffer
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding

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
    override val modifier: CompositionModifier<*> get() = super.modifier as CompositionModifier
    override val children: ArrayList<Composable> = arrayListOf()

    override fun parentWidth(): Float = Aether.instance.displayWidth
    override fun parentHeight(): Float = Aether.instance.displayHeight

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
        modifier.preCompose(this)

        composeSize()
        composePosition()

        // Compose all static components
        children.filterNot(Composable::dynamic).forEach(Composable::compose)
        modifier.compose(this)
        rasterize()
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
        compose()
    }

    /**
     * Converts the active composition layout to a raster image.
     */
    open fun rasterize() {
        if (!modifier.optimizeComposition) return

        if (framebuffer == null || framebuffer!!.width != width || framebuffer!!.height != height) {
            framebuffer = Aether.renderer.createFBO(width, height)
        }

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

    override fun animate(start: DefaultCompositionModifier?, end: DefaultCompositionModifier?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(x, start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(y, start?.y, end?.y, fraction)
        }
        ifNotNull(start?.width, end?.width) {
            width = width ?: 0.px
            width!!.lerp(width, start?.width, end?.width, fraction)
        }
        ifNotNull(start?.height, end?.height) {
            height = height ?: 0.px
            height!!.lerp(height, start?.height, end?.height, fraction)
        }
        ifNotNull(start?.anchorPoint, end?.anchorPoint) {
            anchorPoint = anchorPoint ?: AnchorPoint()
            anchorPoint!!.animate(start?.anchorPoint, end?.anchorPoint, fraction)
        }
        ifNotNull(start?.padding, end?.padding) {
            padding = padding ?: Padding(null, null, null, null)
            padding!!.animate(start?.padding, end?.padding, fraction)
        }
        ifNotNull(start?.margin, end?.margin) {
            margin = margin ?: Margin(null, null, null, null)
            margin!!.animate(start?.margin, end?.margin, fraction)
        }
        ifNotNull(start?.opacity, end?.opacity) {
            opacity = opacity ?: UIAlpha(1f)
            opacity!!.value = lerp(start?.opacity?.value ?: 1f, end?.opacity?.value ?: 1f, fraction)
        }
        ifNotNull(start?.background, end?.background) {
            background = background ?: UIBackground()
            background!!.animate(start?.background, end?.background, fraction)
        }
        if (end != null) {
            optimizeComposition = end.optimizeComposition
            clipContent = end.clipContent
        }
    }
}