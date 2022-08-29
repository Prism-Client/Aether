package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UIAlignment.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.unit.DynamicUnit
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.Padding
import kotlin.math.max

/**
 * [AutoLayout] mimics the Figma Auto Layout feature. In layman's terms, it is a list layout, with more
 * control and combustibility.
 *
 * @author sen
 * @since 1.0
 */
open class AutoLayout(modifier: LayoutModifier<*>) : UILayout(modifier, true) {
    /**
     * Indicates which direction should this align the items within.
     */
    open var layoutAlignment: UIAlignment = CENTER
    open var layoutDirection: LayoutDirection = LayoutDirection.HORIZONTAL
    open var layoutPadding: Padding? = null

    open var itemSpacing: UIUnit<*>? = null

    protected open var potentialSize: Size? = null

    /**
     * Returns the potential size, the size of which the layout is, if all units are static. With this information,
     * dynamic units can figure out their final size and allow the [AutoLayout] to properly lay out the items.
     */
    open fun calculatePotentialSize(): Size {
        var w = 0f
        var h = 0f
        for (child in children) {
            if (layoutDirection == LayoutDirection.HORIZONTAL) {
                w += child.relWidth + itemSpacing.dp
                h = max(h, child.height)
            } else {
                w = max(w, child.width)
                h += child.relHeight + itemSpacing.dp
            }
        }
        return Size(w, h)
    }

    override fun updateUnits() {
        potentialSize = calculatePotentialSize()
        itemSpacing?.compute(true)
        layoutPadding?.compose(this)
        if (itemSpacing is SpaceBetween) {
            itemSpacing!!.value =
                (if (layoutDirection == LayoutDirection.HORIZONTAL) potentialSize!!.width else potentialSize!!.height) / children.size
        }
    }

    override fun updateLayout(): Size {
        val potential = potentialSize!!
        var top = layoutPadding?.top.dp
        var right = layoutPadding?.right.dp
        var bottom = layoutPadding?.bottom.dp
        var left = layoutPadding?.left.dp

        var x = this.x
        var y = this.y

        for (child in children) {
            if (layoutDirection == LayoutDirection.HORIZONTAL) {
                child.x = x
                child.y = y + when (layoutAlignment) {
                    MIDDLELEFT, CENTER, MIDDLERIGHT -> (child.relHeight - potential.height) / 2f
                    BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> (child.relHeight - potential.height)
                    else -> 0f
                }
                x += child.width + itemSpacing.dp
            } else {

            }
        }

        // TODO: Hug layout resize update composable
        if (modifier.width is HugLayout) {
            modifier.width!!.cachedValue = this.x // - itemSpacing.dp
        }

        if (modifier.height is HugLayout) {
            modifier.height!!.cachedValue = this.y // - itemSpacing.dp
        }

//        if (modifier.width is HugLayout || modifier.height is HugLayout) {
//        }

        potentialSize = null
    }
}

/**
 * [SpaceBetween] is an [AutoLayout] specific unit used exclusively for the [AutoLayout.itemSpacing] property. When
 * set, it informs the layout to evenly space the items within it based on the leftover space.
 *
 * @author sen
 * @since 1.0
 */
class SpaceBetween : DynamicUnit<SpaceBetween>(0f) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        if (composable !is AutoLayout)
            throw RuntimeException("The SpaceBetween unit cannot be applied to a ${composable?.javaClass?.simpleName ?: "null"}")
        return 0f
    }

    override fun copy(): SpaceBetween = SpaceBetween()

}

/**
 * [HugLayout] is a [AutoLayout] specific unit used exclusively for the width and height of
 * [AutoLayout]. It informs the layout to resize the given axis to the children within it.
 *
 * @author sen
 * @since 1.0
 */
class HugLayout : DynamicUnit<HugLayout>(0f) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        if (composable !is AutoLayout)
            throw RuntimeException("The HugLayout unit cannot be applied to a ${composable?.javaClass?.simpleName ?: "null"}")
        return 0f
    }

    override fun copy(): HugLayout = HugLayout()
}