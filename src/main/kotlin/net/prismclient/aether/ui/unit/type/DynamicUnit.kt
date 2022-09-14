package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit

/**
 * Adds a hint to the [Composable] when computed that this is depden
 */
abstract class DynamicUnit<T : DynamicUnit<T>>(value: Float) : UIUnit<T>(value) {
    override fun compute(composable: Composable?, width: Float, height: Float, yaxis: Boolean) {
//        if (composable != null) composable.hint()
        super.compute(composable, width, height, yaxis)
    }
}