package net.prismclient.aether.ui.layout

import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.LayoutOrder
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.shorthands.dp

class UIListLayout @JvmOverloads constructor(
    var direction: LayoutDirection,
    var order: LayoutOrder,
    var childSpacing: UIUnit<*>?,
    modifier: UIModifier<*>
) : UILayout(modifier, true) {
    override fun updateLayout() {
        // potential size

        childSpacing.compute(direction == LayoutDirection.VERTICAL)

        val spacing = childSpacing.dp

        var x = x
        var y = y

        if (order == LayoutOrder.FIRST) {
            for (i in 0 until children.size) {
                val child = children[i]

                // Update the position as it would be normally,
                // and only override the axis set.
                child.overridden = false
                child.updatePosition()
                child.overridden = true

                if (direction == LayoutDirection.HORIZONTAL) {
                    child.x = x //+ component.marginTop
                    x += child.width + spacing//component.relHeight + component.marginTop + component.marginBottom + spacing
                } else if (direction == LayoutDirection.VERTICAL) {
                    child.y = y
                    y += child.height + spacing
                }
                child.compose()
            }
        } else {} // TODO: Reverse order direction
    }
}