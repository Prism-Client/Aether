package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.type.solid.PixelUnit

/**
 * [DynamicUnit] is a subtype of [UIUnit] for units which cannot be inferred immediately.
 *
 * For example, a unit which scales based on the size of the
 * content within it. Now, this isn't a big deal if all the [Composable]s within the
 * object are solid units, such as a [PixelUnit] which is known immediately. However,
 * if the unit scales based on say, the width of the object, then it has to be calculated
 * twice. In the case of [Composable]s, all solid (non-dynamic) units are calculated first, then
 * the dynamic units are calculated after the values of the solid units are known.
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