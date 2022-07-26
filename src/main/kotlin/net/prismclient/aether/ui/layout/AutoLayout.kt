package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.alignment.Alignment.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.unit.UIUnit
import kotlin.math.max

/**
 * [AutoLayout] mimics the Figma Auto Layout feature. In layman's terms, it is a list layout, with more
 * control and combustibility.
 *
 * @author sen
 * @since 1.0
 */
class AutoLayout(
    name: String,
    modifier: LayoutModifier<*>,
    layoutStyle: BoxLayoutStyle
) : BoxLayout(name, modifier, layoutStyle) {
    init {
        overrideChildren = true
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
            x += when (layoutStyle.layoutAlignment) {
                TOPCENTER, CENTER, BOTTOMCENTER -> (width - potential.width - right + left) / 2f
                TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> width - potential.width - right + left
                else -> -right
            }.coerceAtLeast(0f)
        } else {
            y += when (layoutStyle.layoutAlignment) {
                MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - potential.height - bottom + top) / 2f
                BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> height - potential.height - bottom + top
                else -> -bottom
            }.coerceAtLeast(0f)
        }

        for (child in children) {
            if (layoutStyle.layoutDirection == LayoutDirection.HORIZONTAL) {
                child.x = x + child.modifier.padding?.left.dp
                child.y = y + when (layoutStyle.layoutAlignment) {
                    MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - child.relHeight - top - bottom) / 2f
                    BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> (height - child.relHeight - top - bottom)
                    else -> 0f
                } + child.modifier.padding?.top.dp
                x += child.relWidth + layoutStyle.itemSpacing.dp
            } else {
                child.x = x + when (layoutStyle.layoutAlignment) {
                    TOPCENTER, CENTER, BOTTOMCENTER -> (width - child.relWidth - left - right) / 2f
                    TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> (width - child.relWidth - left - right)
                    else -> 0f
                } + child.modifier.padding?.left.dp
                child.y = y + child.modifier.padding?.top.dp
                y += child.relHeight + layoutStyle.itemSpacing.dp
            }
            child.compose()
            w = max(child.relX + child.relWidth - this.x, w)
            h = max(child.relY + child.relHeight - this.y, h)
        }

        return Size(w, h)
    }
}

/**
 * [SpaceBetween] is an [AutoLayout] specific unit used exclusively for the [BoxLayoutStyle.itemSpacing] property. When
 * set, it informs the layout to evenly space the items within it based on the leftover space.
 *
 * @author sen
 * @since 1.0
 */
open class SpaceBetween : UIUnit<SpaceBetween>(0f) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        if (composable !is AutoLayout)
            throw RuntimeException("The SpaceBetween unit cannot be applied to a ${composable?.javaClass?.simpleName ?: "null"}")
        return 0f
    }

    override fun copy(): SpaceBetween = SpaceBetween()

    override fun toString(): String = "SpaceBetween($cachedValue)"
}

/**
 * [SpaceEvenly] is a unit used to indicate layouts to space the elements within it evenly. A RuntimeException
 * is thrown if this unit is used anywhere but inside a [UILayout]. Because of the lack of information, the layout
 * has to calculate the value instead of the unit.
 *
 * @author sen
 * @since 1.0
 */
class SpaceEvenly : UIUnit<SpaceEvenly>(0f) {
    override fun compute(composable: Composable?, width: Float, height: Float, yaxis: Boolean) {
        if (composable !is UILayout) throw RuntimeException("Expected a UILayout for a SpaceEvenly unit. Got: $composable")
        super.compute(composable, width, height, yaxis)
    }

    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float = 0f

    override fun copy(): SpaceEvenly = SpaceEvenly()

    override fun toString(): String = "SpaceEvenly()"
}

/**
 * Sets the width and height of this to [HugLayout]. This can only be applied to Auto Layouts.
 */
fun <T : LayoutModifier<*>> T.hug(): T {
    hugWidth()
    hugHeight()
    return this
}

/**
 * Sets the width of this to a [HugLayout]. This can only be applied to Auto Layouts.
 */
fun <T : LayoutModifier<*>> T.hugWidth(): T {
    width = HugLayout()
    return this
}

/**
 * Sets the height of this to a [HugLayout]. This can only be applied to Auto Layouts.
 */
fun <T : LayoutModifier<*>> T.hugHeight(): T {
    height = HugLayout()
    return this
}