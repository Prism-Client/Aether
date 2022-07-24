package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit

/**
 * A [UIUnit] which represents a pixel based on the given [value].
 *
 * @author sen
 * @since 1.0
 */
class UIPixel(value: Float) : UIUnit(value) {
    override fun updateCache(component: UIComponent, yaxis: Boolean): Float = value
}