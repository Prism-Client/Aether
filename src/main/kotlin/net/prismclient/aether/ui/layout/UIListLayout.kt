package net.prismclient.aether.ui.layout

import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.LayoutOrder
import net.prismclient.aether.ui.layout.util.SpaceEvenly
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.shorthands.dp

open class UIListLayout constructor(
    var direction: LayoutDirection,
    var order: LayoutOrder,
    var childSpacing: UIUnit<*>?,
    modifier: UIModifier<*>
) : UILayout(modifier, true) {

    override fun compose() {
        // Compute the spacing prior to any other calculations.
        childSpacing.compute(direction == LayoutDirection.VERTICAL)
        super.compose()
    }

    override fun updateLayout() {
        // TODO: Calculate the potential size of the layout
//        if (dynamic) {
//
//        }
        if (childSpacing is SpaceEvenly) {

        }

        val spacing = childSpacing.dp

        var x = x
        var y = y

        if (order == LayoutOrder.FIRST) {
            for (i in 0 until children.size) {
                val child = children[i]

                // Update the position as it would be normally,
                // and only override the axis set. Update the size as well.
                child.overridden = false
                child.updatePosition()
                child.updateSize()
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
        } else { TODO("Reverse order direction") }
    }
}