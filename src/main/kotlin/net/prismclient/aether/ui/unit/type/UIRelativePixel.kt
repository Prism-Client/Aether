package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit

/**
 * A [UIUnit] which represents the width or height, depending on the axis of the parent
 * times the given [value]. This is considered a dynamic unit, and when necessary Aether
 * will request two composition updates.
 *
 * @author sen
 * @since 1.0
 */
class UIRelativePixel(value: Float) : UIUnit(value) {
    override fun compute(component: UIComponent, yaxis: Boolean) {
        super.compute(component, yaxis)
        component.isDynamic = true
    }

    override fun updateCache(component: UIComponent, yaxis: Boolean): Float = if (yaxis) {
        component.parentHeight
    } else {
        component.parentWidth
    } * value
}