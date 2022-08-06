package net.prismclient.aether.ui.unit.type.dynamic

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.DynamicUnit

/**
 * Acts nearly identical to [RelativeUnit] however, unlike [RelativeUnit] the value scales
 * based on the width/height of the provided width and height instead of the parent of the
 * composable.
 *
 * @author sen
 * @since 1.0
 */
class SizeUnit(value: Float) : DynamicUnit<SizeUnit>(value) {
    override fun updateCache(
        composable: Composable?,
        width: Float,
        height: Float,
        yaxis: Boolean
    ): Float = if (!yaxis) {
        width
    } else {
        height
    } * value

    override fun copy(): SizeUnit = SizeUnit(value)

    override fun toString(): String = "ComposableRelative($value)"
}