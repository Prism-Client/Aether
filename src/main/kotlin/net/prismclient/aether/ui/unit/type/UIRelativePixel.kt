package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.Composable
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
    override fun updateCache(composable: Composable?, yaxis: Boolean): Float {
        composable?.dynamic = true
        return if (yaxis) {
            composable?.parentHeight ?: Aether.instance.displayHeight
        } else {
            composable?.parentWidth ?: Aether.instance.displayWidth
        } * value
    }
}