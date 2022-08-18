package net.prismclient.aether.ui.layout.scroll

import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.property.UIUniqueProperty
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.Overflow
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.shape.Shape

/**
 * [Scrollbar], as the name suggests, represents a Scrollbar which can be applied to a layout on an axis. Because
 * customization is an expected part of UI, [Scrollbar] has multiple implementations which can support animations
 * and other features.
 *
 * Internally, Scrollbar is a [ComposableShape]. [ComposableShape] is a shape which, as the name indicates, is
 * applied to [Composable]s. Because the superclass is a [Shape], there are position and size properties. The
 * position properties act normally; where the x or y is the composable's position plus the given position unit. On
 * the other hand, [Shape.width] or [Shape.height] (depending on the axis) represents the potential width or height
 * of the scrollbar as well as the track (the overall area) of the scrollbar. The thumb
 * (the object used to drag/indicate the scrollbar) will scale it's size based on the Layout's overflow.
 *
 * For the default implementation of the Scrollbar see [DefaultScrollbar].
 *
 * @author sen
 * @since 1.0
 * @see UILayout
 * @see Overflow
 * @see LayoutModifier
 * @see DefaultScrollbar
 */
abstract class Scrollbar : ComposableShape<UILayout>(), UIUniqueProperty<Scrollbar, UILayout> {
    open var direction: LayoutDirection = LayoutDirection.HORIZONTAL

    /**
     * Determined how the scrollbar appears/acts depending on the circumstance.
     *
     * @see Overflow
     */
    open var overflow: Overflow = Overflow.AUTO

    /**
     * Indicates if the scrollbar implementation should skip rendering or not.
     */
    open var shouldRender: Boolean = false

    /**
     * The value of the scrollbar, represented as a value between 0 and 1.
     */
    open var value: Float = 0f

    /**
     * The size of the thumb, known the objet used to scroll. By default, the thumb size
     * is calculated with the following formula:
     *
     *      (size / layoutSize) * scrollbarSize
     */
    open var actualThumbSize: Float = 0f

    /**
     * The offset of the mouse relative to the x or y of the thumb
     */
    protected open var mouseOffset: Float = 0f

    override fun compose(composable: UILayout?) {
        super.compose(composable); composable!!

        // Compute if the scrollbar should be
        // rendered and the size of the thumb.
        if (direction == LayoutDirection.HORIZONTAL) {
            shouldRender = when (overflow) {
                Overflow.SCROLLBAR -> true
                Overflow.AUTO -> composable.widthOverflow() > 0f
                else -> false
            }
            actualThumbSize = (composable.width / composable.layoutSize.width.coerceAtLeast(composable.width)) * width.dp
        } else {
            shouldRender = when (overflow) {
                Overflow.SCROLLBAR -> true
                Overflow.AUTO -> composable.heightOverflow() > 0f
                else -> false
            }
            actualThumbSize = (composable.height / (composable.layoutSize.height.coerceAtLeast(composable.height))) * height.dp
        }

    }
}

/**
 * @author sen
 * @since 1.0
 */
class DefaultScrollbar : Scrollbar() {
    var thumbColor: UIColor? = null
    var background: UIBackground? = null

    override fun render() {
        if (!shouldRender) return
        background?.render()
        var x = initialX + x.dp
        var y = initialY + y.dp
        var w = width.dp
        var h = height.dp

        // Compute the offset
        if (direction == LayoutDirection.HORIZONTAL) {
            x += (width.dp - actualThumbSize) * value
            w = actualThumbSize
        } else {
            y += (height.dp - actualThumbSize) * value
            h = actualThumbSize
        }

        renderer {
            color(thumbColor)
            rect(x, y, w, h)
        }
    }

    override fun copy(): Scrollbar {
        TODO("Not yet implemented")
    }

    override fun merge(other: Scrollbar?) {
        TODO("Not yet implemented")
    }

    override fun animate(start: Scrollbar?, end: Scrollbar?, fraction: Float) {
        TODO("Not yet implemented")
    }
}