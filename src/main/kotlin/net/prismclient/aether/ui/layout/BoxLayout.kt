package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.composer.ComposableContext
import net.prismclient.aether.ui.composer.Context
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.style.Style
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.compute
import net.prismclient.aether.ui.unit.other.Padding
import net.prismclient.aether.ui.unit.typeOf
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * [BoxLayout] is one of the simplest types of layouts. It does nothing to the elements within it.
 */
open class BoxLayout(
    name: String,
    modifier: LayoutModifier<*>,
    val layoutStyle: BoxLayoutStyle
): UILayout(name, modifier, false) {
    open var potentialSize: Size? = null
        protected set

    /**
     * Calculates the potential layout size. The width or height are summed depending
     * on the [BoxLayoutStyle.layoutDirection], and the other axis is the largest element within
     * layout. It also incorporates for the [BoxLayoutStyle.layoutPadding].
     */
    open fun calculatePotentialSize(): Size {
        var width = 0f
        var height = 0f

        for (child in children) {
            child.compose(Context.createContext(child))
            if (layoutStyle.layoutDirection == LayoutDirection.HORIZONTAL) {
                width += child.relWidth + layoutStyle.itemSpacing.dp
                height = max(height, child.relHeight)
            } else {
                width = max(width, child.relWidth)
                height += child.relHeight + layoutStyle.itemSpacing.dp
            }
        }

        // Remove the item spacing from the last element.
        if (layoutStyle.layoutDirection == LayoutDirection.HORIZONTAL) {
            width -= layoutStyle.itemSpacing.dp
        } else {
            height -= layoutStyle.itemSpacing.dp
        }

        return Size(
            width + layoutStyle.layoutPadding?.left.dp + layoutStyle.layoutPadding?.right.dp,
            height + layoutStyle.layoutPadding?.top.dp + layoutStyle.layoutPadding?.bottom.dp
        )
    }

    override fun compose(context: ComposableContext) {
        // Calculate hte potential size of the layout so the functions
        // invoked from compose such as updateLayout and updateSize will
        // have a general idea of the final dimensions of the layout.
        if (modifier.width.typeOf(HugLayout::class) || modifier.height.typeOf(HugLayout::class)) potentialSize = calculatePotentialSize()
        super.compose(context)
        potentialSize = null
    }

    override fun composeSize(context: ComposableContext) {
        super.composeSize(context)

        // If the layout is intended to be resized based on the size of the
        // layout, increment equal the cachedValue of it to the potential
        // size of the layout. Because HugLayout can be used within other
        // units such as DynamicUnits, it is increment as a value for the
        // other unit might be stored there which shouldn't be overwritten.
        if (modifier.width.typeOf(HugLayout::class) || modifier.height.typeOf(HugLayout::class)) {
            if (modifier.width.typeOf(HugLayout::class)) {
                modifier.width!!.cachedValue += potentialSize!!.width
            }
            if (modifier.height.typeOf(HugLayout::class)) {
                modifier.height!!.cachedValue += potentialSize!!.height
            }

            width = modifier.width.dp.roundToInt().toFloat()
            height = modifier.height.dp.roundToInt().toFloat()
            modifier.composePadding(context)
        }
    }

    override fun updateUnits(context: ComposableContext) {
        layoutStyle.compose(context)
    }

    override fun updateLayout(): Size {
        var w = 0f
        var h = 0f

        children.forEach { child ->
            child.compose(Context.createContext(child))
            w = max(child.x + child.relWidth - this.x, w)
            h = max(child.y + child.relHeight - this.y, h)
        }

        return Size(w, h)
    }
}

/**
 * [BoxLayoutStyle] contains styling information for an [BoxLayoutStyle]. It provides properties similar
 * to the Figma Auto Layout feature.
 *
 * @author sen
 * @since 1.0
 */
class BoxLayoutStyle : Style<BoxLayoutStyle, BoxLayout>() {
    /**
     * The direction which the content should be laid.
     */
    var layoutAlignment: Alignment = Alignment.TOPLEFT

    /**
     * The axis, or direction of which the layout should be laid.
     */
    var layoutDirection: LayoutDirection = LayoutDirection.HORIZONTAL

    /**
     * The layout padding represents the spacing on each side of the layout. It contributes to figuring
     * out the potential size; however, if the size of the layout is a fixed layout, independent of the
     * layout size (pretty much anything that isn't [HugLayout]), only the [Padding.left] and [Padding.top]
     * will be the only ones to have a visible effect on the layout.
     *
     * @see itemSpacing
     */
    var layoutPadding: Padding? = null

    /**
     * The spacing between each child. This does not include the first and end child.
     *
     * @see layoutPadding
     */
    var itemSpacing: UIUnit<*>? = null

    override fun compose(context: ComposableContext) {
        val layout = context.composable as BoxLayout
        itemSpacing.compute(context, layoutDirection == LayoutDirection.VERTICAL)
        layoutPadding?.compose(context)
        if (itemSpacing is SpaceBetween)
            itemSpacing!!.value = if (layoutDirection == LayoutDirection.HORIZONTAL) {
                layout.potentialSize!!.width
            } else {
                layout.potentialSize!!.height
            } / layout.children.size.coerceAtLeast(0)
    }

    override fun animate(context: AnimationContext<*>, start: BoxLayoutStyle?, end: BoxLayoutStyle?, progress: Float) {
        TODO("Not yet implemented")
    }

    override fun copy(): BoxLayoutStyle = BoxLayoutStyle().also {
        it.layoutAlignment = layoutAlignment
        it.layoutDirection = layoutDirection
        it.layoutPadding = layoutPadding.copy
        it.itemSpacing = itemSpacing.copy
    }

    override fun merge(other: BoxLayoutStyle?) {
        if (other != null) {
            layoutAlignment = other.layoutAlignment
            layoutDirection = other.layoutDirection
            layoutPadding = other.layoutPadding or layoutPadding
            itemSpacing = other.itemSpacing or itemSpacing
        }
    }

    // -- Extension Functions -- //

    /**
     * Sets the [layoutAlignment] of the layout. It defines how the content should be laid.
     */
    fun layoutAlignment(alignment: Alignment) = apply {
        layoutAlignment = alignment
    }

    /**
     * Sets the [layoutAlignment] of the layout. It defines how the content shou
     */
    fun alignment(alignment: Alignment) = layoutAlignment(alignment)

    /**
     * Sets the [layoutDirection] of the layout. This determines the axis of which layout should be laid.
     */
    fun layoutDirection(direction: LayoutDirection) = apply { layoutDirection = direction }

    /**
     * Sets the [layoutDirection] of the layout. This determines the axis of which layout should be laid.
     */
    fun direction(direction: LayoutDirection) = layoutDirection(direction)

    /**
     * Sets the [layoutPadding] to the given [padding].
     */
    fun layoutPadding(padding: Padding) = apply { layoutPadding = padding }

    /**
     * Sets the [layoutPadding] to the given [padding].
     */
    fun padding(padding: Padding) = apply { layoutPadding = padding }

    /**
     * Sets the item spacing to the given [spacing]
     *
     * @see itemSpacing
     */
    fun layoutSpacing(spacing: UIUnit<*>?) = apply { itemSpacing = spacing }

    /**
     * Sets the item spacing to the given [spacing].
     *
     * @see spacing
     */
    fun itemSpacing(spacing: UIUnit<*>?) = layoutSpacing(spacing)

    /**
     * Sets the item spacing to the given [spacing]
     *
     * @see itemSpacing
     */
    fun spacing(spacing: UIUnit<*>?) = apply { itemSpacing = spacing }
}

/**
 * [HugLayout] is a [AutoLayout] specific unit used exclusively for the width and height of [BoxLayout]s
 * and [AutoLayout]s. It informs the layout to resize the given axis to the children within it.
 *
 * @author sen
 * @since 1.0
 */
open class HugLayout : UIUnit<HugLayout>(0f) {
    override fun updateCache(context: ComposableContext?, width: Float, height: Float, yaxis: Boolean): Float {
        if (context?.composable !is BoxLayout)
            throw RuntimeException("The HugLayout unit cannot be applied to a ${context?.composable?.javaClass?.simpleName}")
        return 0f
    }

    override fun copy(): HugLayout = HugLayout()

    override fun toString(): String = "HugLayout($cachedValue)"
}