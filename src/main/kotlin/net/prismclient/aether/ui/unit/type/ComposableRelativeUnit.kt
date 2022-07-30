package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.shorthands.dp

/**
 * Acts nearly identical to [RelativeUnit] however, unlike [RelativeUnit] the value scales
 * based on the width/height of the composable instead of the parent of the composable. This
 * is considered a dynamic unit.
 *
 * @author sen
 * @since 1.0
 */
class ComposableRelativeUnit(value: Float) : UIUnit<ComposableRelativeUnit>(value) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        composable?.dynamic = true
        return if (!yaxis) {
            composable?.width?.dp ?: Aether.instance.displayWidth
        } else {
            composable?.height?.dp ?: Aether.instance.displayHeight
        } * value
    }

    override fun copy(): ComposableRelativeUnit = ComposableRelativeUnit(value)

    override fun toString(): String = "ComposableRelative($value)"
}