package net.prismclient.aether.ui.layout.scroll

import net.prismclient.aether.core.util.property.UIProperty
import net.prismclient.aether.core.util.property.UIUniqueProperty
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.Overflow
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.shape.Shape
import java.util.function.Consumer

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
    var direction: LayoutDirection = LayoutDirection.HORIZONTAL

    /**
     * Determined how the scrollbar appears/acts depending on the circumstance.
     *
     * @see Overflow
     */
    var overflow: Overflow = Overflow.AUTO

    /**
     * The value of the scrollbar, represented as a value between 0 and 1.
     */
    var value: Double = 0.0

    /**
     * The size of the thumb, known the objet used to scroll. By default, the thumb size
     * is calculated with the following formula:
     *
     *      (size / layoutSize) * scrollbarSize
     */
    var thumbSize: Float = 0f

    override fun compose(composable: UILayout?) {
        super.compose(composable); composable!!

        thumbSize = if (direction == LayoutDirection.HORIZONTAL)
                    (composable.width / composable.layoutSize.width) * width.dp
            else    (composable.height / composable.layoutSize.height) * height.dp
    }
}

/**
 * @author sen
 * @since 1.0
 */
class DefaultScrollbar