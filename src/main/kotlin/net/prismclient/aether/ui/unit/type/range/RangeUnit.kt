package net.prismclient.aether.ui.unit.type.range

import net.prismclient.aether.core.util.shorthands.copy
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import kotlin.reflect.KClass

/**
 * Acts like [unit] however, the cached value will not exceed the [min] and [max] bounds. If either bounds
 * are null, it is ignored. [UIUnit.identifiesAs] is overwritten and returns [unit]s output.
 *
 * @author sen
 * @since 1.0
 */
class RangeUnit(var unit: UIUnit<*>, var min: UIUnit<*>?, var max: UIUnit<*>?) : UIUnit<RangeUnit>(0f) {
    override var cachedValue: Float = super.cachedValue
        set(value) {
            field = value
                .coerceAtLeast(min?.cachedValue ?: value)
                .coerceAtMost(max?.cachedValue ?: value)
        }

    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        unit.compute(composable, width, height, yaxis)
        min?.compute(composable, width, height, yaxis)
        max?.compute(composable, width, height, yaxis)
        return unit.dp
    }

    override fun copy(): RangeUnit = RangeUnit(unit.copy(), min.copy, max.copy)

    override fun toString(): String = "RangeUnit($unit, $min, $max)"

    override fun <T : UIUnit<T>> identifiesAs(clazz: KClass<T>): Boolean = unit.identifiesAs(clazz)
}