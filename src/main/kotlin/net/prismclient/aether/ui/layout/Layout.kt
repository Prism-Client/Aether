package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.color.UIAlpha
import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.property.Focusable
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.layout.scroll.DefaultScrollbar
import net.prismclient.aether.ui.layout.scroll.Scrollbar
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding

/**
 * [UILayout] is composition subset designed for controlling a group of components in a specific way. It is a Focusable
 * composable, as it introduces the opportunity for scrollbars.
 *
 * //todo: doc this stuff xd
 * //todo: automatically calculate the layout size after composition
 *
 * @author sen
 * @since 1.0
 *
 * @param overrideChildren When true, the children's property overridden is enabled during the compose stage.
 */
abstract class UILayout(
    name: String,
    modifier: LayoutModifier<*>,
    @Suppress("MemberVisibilityCanBePrivate") protected var overrideChildren: Boolean
) : Composition(name, modifier), ComposableGroup, Focusable {
    override val modifier: LayoutModifier<*> = super.modifier as LayoutModifier<*>

    override val children: ArrayList<Composable> = arrayListOf()

    /**
     * The size of the layout which is set after [updateLayout] is invoked.
     */
    open lateinit var layoutSize: Size

    /**
     * Returns the width of area exceeding the bounds of this layout, or 0.
     */
    open fun widthOverflow(): Float = (layoutSize.width - width).coerceAtLeast(0f)

    /**
     * Returns the height of area exceeding the bounds of this layout, or 0.
     */
    open fun heightOverflow(): Float = (layoutSize.height - height).coerceAtLeast(0f)

    override fun xOffset(): Float =
        (modifier.horizontalScrollbar?.value ?: 0f) * (layoutSize.width - this.width) + super.xOffset()

    override fun yOffset(): Float =
        (modifier.verticalScrollbar?.value ?: 0f) * (layoutSize.height - this.height) + super.yOffset()

    override fun compose() {
        modifier.preCompose(this)
        composeSize()
        composePosition()
        // Invoke the updateUnits function after
        // calculating the relevant properties of this.
        updateUnits()

        // Update the parent and override (if necessary) to the children
        children.forEach {
            it.parent = this
            if (overrideChildren) it.overridden = true
        }
        // Calculate the initial and possible the final layout
        layoutSize = updateLayout()

        modifier.compose(this)
        rasterize()
    }

    /**
     * Update any units of the layout. The potential layout size is already calculated
     * at this point if necessary.
     *
     * @see layoutSize
     */
    abstract fun updateUnits()

    /**
     * Invoked when the layout needs an update. At this point, [updateUnits] has already been invoked.
     *
     * @return Expects the size of the layout with the origin point as the (x, y) of this.
     */
    abstract fun updateLayout(): Size

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
                modifier.preRender()
                // Calculate the offset of the scrollbars
                val xOffset = (modifier.horizontalScrollbar?.value ?: 0f) * (layoutSize.width - width)
                val yOffset = (modifier.verticalScrollbar?.value ?: 0f) * (layoutSize.height - height)

                // Translate by the offset prior to rendering
                renderer.translate(-xOffset, -yOffset)
                children.forEach {
                    // A Composable might make their own scissor calls, which
                    // can mess up the rendering for the next Composable.
                    if (modifier.clipContent)
                        renderer.scissor(relX + xOffset, relY + yOffset, relWidth, relHeight)
                    it.render()
                }
                renderer.restore()
                modifier.render()
            }
        }
    }

    override fun rasterize() {
        if (!modifier.optimizeComposition) return

        if (framebuffer == null || framebuffer!!.width != width || framebuffer!!.height != height) {
            framebuffer = Aether.renderer.createFBO(width, height)
        }

        UIRendererDSL.renderToFramebuffer(framebuffer!!) {
            renderer.save()
            translate(-x, -y) {
                shouldSave = false
                modifier.preRender()

                // Calculate the offset of the scrollbars
                val xOffset = (modifier.horizontalScrollbar?.value ?: 0f) * (layoutSize.width - width)
                val yOffset = (modifier.verticalScrollbar?.value ?: 0f) * (layoutSize.height - height)

                // Translate by the offset prior to rendering
                renderer.translate(-xOffset, -yOffset)
                children.forEach(Composable::render)
                // Return by inverting the value and render the scrollbar
                renderer.translate(xOffset, yOffset)

                modifier.render()
                shouldSave = true
            }
            renderer.restore()
        }
    }

    /**
     * Return true if at least one scrollbar is not null.
     */
    override fun wantsFocus(): Boolean = modifier.horizontalScrollbar != null || modifier.verticalScrollbar != null
}

/**
 * [LayoutModifier] the [UIModifier] type required for [UILayout]s. It is an abstract class, and holds the
 * properties for deciding what to do with overflowing content within layouts. [T] is expected to be the class
 * which extends this.
 *
 * @author sen
 * @since 1.0
 * @see DefaultLayoutModifier
 */
abstract class LayoutModifier<T : LayoutModifier<T>> : CompositionModifier<T>() {
    open var horizontalScrollbar: Scrollbar? = null
        set(value) {
            value?.direction = LayoutDirection.HORIZONTAL
            field = value
        }
    open var verticalScrollbar: Scrollbar? = null
        set(value) {
            value?.direction = LayoutDirection.VERTICAL
            field = value
        }

    override fun compose(composable: Composable) {
        super.compose(composable)

        // Update the scrollbars after the layout has been updated
        horizontalScrollbar?.compose(composable as UILayout)
        verticalScrollbar?.compose(composable as UILayout)
    }

    override fun render() {
        horizontalScrollbar?.render()
        verticalScrollbar?.render()
    }

    // -- Extension Functions -- //
}

/**
 * Returns the default implementation of a [LayoutModifier].
 */
@Suppress("FunctionName")
fun LayoutModifier(): DefaultLayoutModifier = DefaultLayoutModifier()

class DefaultLayoutModifier : LayoutModifier<DefaultLayoutModifier>() {
    override fun copy(): DefaultLayoutModifier = DefaultLayoutModifier().also {
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
        it.horizontalScrollbar = horizontalScrollbar.copy
        it.verticalScrollbar = verticalScrollbar.copy
    }

    override fun merge(other: DefaultLayoutModifier?) {
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
            horizontalScrollbar = other.horizontalScrollbar or horizontalScrollbar
            verticalScrollbar = other.verticalScrollbar or verticalScrollbar
        }
    }

    override fun animate(start: DefaultLayoutModifier?, end: DefaultLayoutModifier?, fraction: Float) {
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
        ifNotNull(start?.horizontalScrollbar, end?.horizontalScrollbar) {
            horizontalScrollbar = horizontalScrollbar ?: DefaultScrollbar()
            horizontalScrollbar!!.animate(start?.horizontalScrollbar, end?.horizontalScrollbar, fraction)
        }
        ifNotNull(start?.verticalScrollbar, end?.verticalScrollbar) {
            verticalScrollbar = verticalScrollbar ?: DefaultScrollbar()
            verticalScrollbar!!.animate(start?.verticalScrollbar, end?.verticalScrollbar, fraction)
        }
    }
}