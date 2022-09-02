package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UIAlignment.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.style.Style
import net.prismclient.aether.ui.unit.DynamicUnit
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.Padding
import net.prismclient.aether.ui.unit.typeOf
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * [AutoLayout] mimics the Figma Auto Layout feature. In layman's terms, it is a list layout, with more
 * control and combustibility.
 *
 * @author sen
 * @since 1.0
 */
open class AutoLayout(
    layoutName: String,
    modifier: LayoutModifier<*>,
    open protected val layoutStyle: AutoLayoutStyle
) : UILayout(layoutName, modifier, true) {
    protected open var potentialSize: Size? = null

    /**
     * Calculates the potential layout size. The width or height are summed depending
     * on the [layoutDirection], and the other axis is the largest element within
     * layout. It also incorporates for the [layoutPadding].
     */
    open fun calculatePotentialSize(): Size {
        var width = 0f
        var height = 0f

        for (child in children) {
            child.compose()
            if (layoutDirection == LayoutDirection.HORIZONTAL) {
                width += child.relWidth + itemSpacing.dp
                height = max(height, child.height)
            } else {
                width = max(width, child.relWidth)
                height += child.relHeight + itemSpacing.dp
            }
        }

        // Remove the item spacing from the last element.
        if (layoutDirection == LayoutDirection.HORIZONTAL) {
            width -= itemSpacing.dp
        } else {
            height -= itemSpacing.dp
        }

        return Size(
            width + layoutPadding?.left.dp + layoutPadding?.right.dp,
            height + layoutPadding?.top.dp + layoutPadding?.bottom.dp
        )
    }

    override fun compose() {
        // Calculate hte potential size of the layout so the functions
        // invoked from compose such as updateLayout and updateSize will
        // have a general idea of the final dimensions of the layout.
        potentialSize = calculatePotentialSize()
        super.compose()
        potentialSize = null
    }

    override fun composeSize() {
        super.composeSize()

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
            composePadding()
        }
    }

    override fun updateUnits() {
        layoutStyle.compose(this)
        itemSpacing?.compute(true)
        layoutPadding?.compose(this)
        if (itemSpacing is SpaceBetween) {
            itemSpacing!!.value =
                (if (layoutDirection == LayoutDirection.HORIZONTAL) potentialSize!!.width else potentialSize!!.height) / children.size
        }
    }

    override fun updateLayout(): Size {
        val potential = potentialSize!!
        val top = layoutStyle.layoutPadding?.top.dp
        val right = layoutStyle.layoutPadding?.right.dp
        val bottom = layoutStyle.layoutPadding?.bottom.dp
        val left = layoutStyle.layoutPadding?.left.dp

        var x = this.x + left
        var y = this.y + top
        var w = 0f
        var h = 0f

        // Offset the axis of which the layout is directed
        // based on the alignment and the leftover space.
        if (layoutStyle.layoutDirection == LayoutDirection.HORIZONTAL) {
            x += when (layoutAlignment) {
                TOPCENTER, CENTER, BOTTOMCENTER -> (width - potential.width - right + left) / 2f
                TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> width - potential.width - right + left
                else -> -right
            }.coerceAtLeast(0f)
        } else {
            y += when (layoutAlignment) {
                MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - potential.height - bottom + top) / 2f
                BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> height - potential.height - bottom + top
                else -> -bottom
            }.coerceAtLeast(0f)
        }

        for (child in children) {
            if (layoutStyle.layoutDirection == LayoutDirection.HORIZONTAL) {
                child.x = x
                child.y = y + when (layoutStyle.layoutAlignment) {
                    MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - child.relHeight - top - bottom) / 2f
                    BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> (height - child.relHeight - top - bottom)
                    else -> 0f
                }
                x += child.relWidth + itemSpacing.dp
            } else {
                child.x = x + when (layoutStyle.layoutAlignment) {
                    TOPCENTER, CENTER, BOTTOMCENTER -> (width - child.relWidth - left - right) / 2f
                    TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> (width - child.relWidth - left - right)
                    else -> 0f
                }
                child.y = y
                y += child.relHeight + itemSpacing.dp
            }
            child.compose()
            w = max(child.x + child.relWidth - this.x, w)
            h = max(child.y + child.relHeight - this.y, h)
        }

        return Size(w, h)
    }
}

/**
 * [AutoLayoutStyle] contains styling information for an [AutoLayout]. It provides properties similar
 * to the Figma Auto Layout feature.
 *
 * @author sen
 * @since 1.0
 */
class AutoLayoutStyle : Style<AutoLayoutStyle, AutoLayout>() {
    /**
     * The direction which the content should be laid.
     */
    var layoutAlignment: UIAlignment = TOPLEFT

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

    override fun animate(start: AutoLayoutStyle?, end: AutoLayoutStyle?, fraction: Float) {
        TODO("Not yet implemented")
    }

    override fun compose(composable: AutoLayout?) {
        TODO("Not yet implemented")
    }

    override fun copy(): AutoLayoutStyle {
        TODO("Not yet implemented")
    }

    override fun merge(other: AutoLayoutStyle?) {
        TODO("Not yet implemented")
    }
}

/**
 * [paceBetween] is an [AutoLayout] specific unit used exclusively for the [AutoLayout.itemSpacing] property. When
 * set, it informs the layout to evenly space the items within it based on the leftover space.
 *
 * @author sen
 * @since 1.0
 */
open class SpaceBetween : DynamicUnit<SpaceBetween>(0f) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        if (composable !is AutoLayout)
            throw RuntimeException("The SpaceBetween unit cannot be applied to a ${composable?.javaClass?.simpleName ?: "null"}")
        return 0f
    }

    override fun copy(): SpaceBetween = SpaceBetween()

    override fun toString(): String = "SpaceBetween($cachedValue)"
}

/**
 * [HugLayout] is a [AutoLayout] specific unit used exclusively for the width and height of
 * [AutoLayout]. It informs the layout to resize the given axis to the children within it.
 *
 * @author sen
 * @since 1.0
 */
open class HugLayout : UIUnit<HugLayout>(0f) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        if (composable !is AutoLayout)
            throw RuntimeException("The HugLayout unit cannot be applied to a ${composable?.javaClass?.simpleName ?: "null"}")
        return 0f
    }

    override fun copy(): HugLayout = HugLayout()

    override fun toString(): String = "HugLayout($cachedValue)"
}

/**
 * Sets the width and height of this to [HugLayout]. This can only be applied to Auto Layouts.
 */
fun <T : LayoutModifier<T>> T.hug(): T {
    hugWidth()
    hugHeight()
    return this
}

/**
 * Sets the width of this to a [HugLayout]. This can only be applied to Auto Layouts.
 */
fun <T : LayoutModifier<T>> T.hugWidth(): T {
    width = HugLayout()
    return this
}

/**
 * Sets the height of this to a [HugLayout]. This can only be applied to Auto Layouts.
 */
fun <T : LayoutModifier<T>> T.hugHeight(): T {
    height = HugLayout()
    return this
}