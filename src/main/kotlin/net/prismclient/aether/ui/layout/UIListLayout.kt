package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.LayoutOrder
import net.prismclient.aether.ui.unit.UIUnit

open class UIListLayout constructor(
    layoutName: String,
    var direction: LayoutDirection,
    var order: LayoutOrder,
    var itemSpacing: UIUnit<*>?,
    modifier: LayoutModifier<*>
) : UILayout(layoutName, modifier, true) {
    override fun updateUnits() {
        itemSpacing.compute(direction == LayoutDirection.VERTICAL)
    }

    override fun updateLayout(): Size {
        val spacing = itemSpacing.dp

        var x = x
        var y = y

        var w = 0f
        var h = 0f

        if (order == LayoutOrder.FIRST) {
            for (i in 0 until children.size) {
                val child = children[i]

                // Update the position as it would be normally,
                // and only override the axis set. Update the size as well.
                child.overridden = false
                child.compose()
                child.overridden = true

                if (direction == LayoutDirection.HORIZONTAL) {
                    child.x = x + child.modifier.padding?.left.dp
                    x += child.relWidth + spacing
                } else if (direction == LayoutDirection.VERTICAL) {
                    child.y = y + child.modifier.padding?.top.dp
                    y += child.relHeight + spacing
                }
                child.compose()

                w = w.coerceAtLeast(child.x + child.relWidth - this.x)
                h = h.coerceAtLeast(child.y + child.relHeight - this.y)
            }
        } else {
            TODO("Reverse order direction")
        }

        // Remove the extra space calculated at the last child
        if (direction == LayoutDirection.HORIZONTAL) {
            w -= spacing
        } else {
            h -= spacing
        }

        return Size(w, h)
    }
}