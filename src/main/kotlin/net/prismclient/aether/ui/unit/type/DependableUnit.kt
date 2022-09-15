package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import java.util.function.Supplier

/**
 * [DependableUnit] expects a [Supplier] which is executed when the unit is calculated. The returned
 * value is the cached value.
 *
 * @author sen
 * @since 1.0
 */
class DependableUnit(val supplier: Supplier<Float>) : UIUnit<DependableUnit>(0f) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float = supplier.get()

    override fun copy(): DependableUnit = DependableUnit(supplier)
}

fun Dependent(supplier: Supplier<Float>): DependableUnit = DependableUnit(supplier)