package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.type.solid.PixelUnit

/**
 * Indicates to Aether that the unit is a [DynamicUnit]. Dynamic units, in essence, are units which rely on a property
 * which **may** have not yet been calculated. For example, if a unit scales based on the children within it (such as a
 * layout), the final size of the layout will not be known until the layout is updated once. To combat this, every
 * property is set, and if a dynamic property is found, the layout will compose twice.
 *
 * @author sen
 * @since 1.0
 */
abstract class DynamicUnit<T : DynamicUnit<T>>(value: Float) : UIUnit<T>(value) {
    override fun compute(composable: Composable?, width: Float, height: Float, yaxis: Boolean) {
        composable?.dynamic = true
        super.compute(composable, width, height, yaxis)
    }
}