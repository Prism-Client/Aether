package net.prismclient.aether.ui.layout.scroll

import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.event.MouseMove
import net.prismclient.aether.core.event.MousePress
import net.prismclient.aether.core.event.MouseRelease
import net.prismclient.aether.core.event.MouseScrolled
import net.prismclient.aether.core.util.property.UIUniqueProperty
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.core.util.shorthands.within
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.Overflow
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.shape.Shape
import net.prismclient.aether.ui.unit.other.UIRadius

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
    protected open lateinit var composable: Composable

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
        set(value) {
            field = value.coerceAtLeast(0f)
                .coerceAtMost(1f)
        }

    /**
     * The size of the thumb, known the objet used to scroll. By default, the thumb size
     * is calculated with the following formula:
     *
     *      (size / layoutSize) * scrollbarSize
     */
    open var actualThumbSize: Float = 0f

    /**
     * Indicates if the thumb of this scrollbar is actively selected, which is defined as if
     * the mouse was pressed, and has not been release within the bounds of the thumb.
     */
    open var thumbSelected: Boolean = false

    override fun compose(composable: UILayout?) {
        super.compose(composable); this.composable = composable!!

        // Compute if the scrollbar should be
        // rendered and the size of the thumb.
        if (direction == LayoutDirection.HORIZONTAL) {
            shouldRender = when (overflow) {
                Overflow.SCROLLBAR -> true
                Overflow.AUTO -> composable.widthOverflow() > 0f
                else -> false
            }
            actualThumbSize =
                (composable.width / composable.layoutSize.width.coerceAtLeast(composable.width)) * width.dp
        } else {
            shouldRender = when (overflow) {
                Overflow.SCROLLBAR -> true
                Overflow.AUTO -> composable.heightOverflow() > 0f
                else -> false
            }
            actualThumbSize =
                (composable.height / (composable.layoutSize.height.coerceAtLeast(composable.height))) * height.dp
        }
        composable.addListener("$this:$direction", listener = ::onScroll)
        composable.addListener("$this:$direction", listener = ::onMousePress)
        composable.addListener("$this:$direction", listener = ::onMouseRelease)
        composable.addListener("$this:$direction", listener = ::onMouseMove)
    }

    /**
     * Invoked when the mouse is scrolled. [event] gives the necessary
     * data to determine how to adjust the scrollbar.
     */
    open fun onScroll(event: MouseScrolled) {
        value -= if (direction == LayoutDirection.HORIZONTAL) {
            event.dstX / width.dp.coerceAtLeast(1f)
        } else {
            event.dstY / height.dp.coerceAtLeast(1f)
        }
    }

    /**
     * Invoked whe the mouse is pressed, within the [compose] function.
     */
    abstract fun onMousePress(event: MousePress)

    /**
     * Invoked when the mouse is released, within the [compose] function.
     */
    open fun onMouseRelease(event: MouseRelease) {
        thumbSelected = false
    }

    /**
     * Invoked when the mouse is moved, within the [compose] function,
     */
    abstract fun onMouseMove(event: MouseMove)
}

/**
 * Creates a new [DefaultScrollbar] amd applies the [block] tp it.
 */
inline fun Scrollbar(block: Block<DefaultScrollbar>): DefaultScrollbar = DefaultScrollbar().apply(block)

/**
 * @author sen
 * @since 1.0
 */
class DefaultScrollbar : Scrollbar() {
    var thumbColor: UIColor? = null
    var thumbRadius: UIRadius? = null
    var background: UIBackground? = null

    var thumbBounds: FloatArray = FloatArray(4)

    /**
     * The offset of the mouse relative to the x or y of the thumb
     */
    private var mouseOffset: Float = 0f

    override var value: Float = super.value
        set(value) {
            field = value.coerceAtLeast(0f).coerceAtMost(1f)
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
            thumbBounds[0] = x
            thumbBounds[1] = y
            thumbBounds[2] = w
            thumbBounds[3] = h
        }

    override fun compose(composable: UILayout?) {
        super.compose(composable)
        background?.x = x
        background?.y = y
        background?.width = width
        background?.height = height
        background?.compose(composable)
        thumbRadius?.compose(composable)
        value = value // Update the thumbBounds
    }

    override fun render() {
        if (!shouldRender) return
        background?.render()
        Renderer {
            color(thumbColor)
            rect(thumbBounds[0], thumbBounds[1], thumbBounds[2], thumbBounds[3], thumbRadius)
        }
    }

    override fun onMousePress(event: MousePress) {
        if (within(event.mouseX, event.mouseY, thumbBounds[0], thumbBounds[1], thumbBounds[2], thumbBounds[3])) {
            thumbSelected = true
            mouseOffset = if (direction == LayoutDirection.HORIZONTAL)
                event.mouseX - thumbBounds[0]
            else event.mouseY - thumbBounds[1]
        }
    }

    override fun onMouseMove(event: MouseMove) {
        if (thumbSelected) {
            value = if (direction == LayoutDirection.HORIZONTAL)
                (event.mouseX - initialX - x.dp - mouseOffset) / (width.dp - actualThumbSize)
            else (event.mouseY - initialY - y.dp - mouseOffset) / (height.dp - actualThumbSize)
            composable.composition.recompose()
        }
    }

    override fun animate(
        context: AnimationContext<*>,
        initial: Scrollbar?,
        start: Scrollbar?,
        end: Scrollbar?,
        progress: Float,
        completed: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun copy(): Scrollbar {
        TODO("Not yet implemented")
    }

    override fun merge(other: Scrollbar?) {
        TODO("Not yet implemented")
    }
}