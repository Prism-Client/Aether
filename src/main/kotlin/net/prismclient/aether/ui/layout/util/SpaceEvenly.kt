package net.prismclient.aether.ui.layout.util

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.unit.DynamicUnit
import net.prismclient.aether.ui.unit.UIUnit

/**
 * [SpaceEvenly] is a unit used to indicate layouts to space the elements within it evenly. A RuntimeException
 * is thrown if this unit is used anywhere but inside a [UILayout]. Because of the lack of information, the layout
 * has to calculate the value instead of the unit.
 *
 * @author sen
 * @since 1.0
 */
class SpaceEvenly : DynamicUnit<SpaceEvenly>(0f) {
    override fun compute(composable: Composable?, width: Float, height: Float, yaxis: Boolean) {
        if (composable !is UILayout) throw RuntimeException("Expected a UILayout for a SpaceEvenly unit. Got: $composable")
        super.compute(composable, width, height, yaxis)
    }

    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float = 0f

    override fun copy(): SpaceEvenly = SpaceEvenly()

    override fun toString(): String = "SpaceEvenly()"
}