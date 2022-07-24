package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.composition.Composable

/**
 * @author sen
 * @since 1.0
 */
abstract class UIUnit(var value: Float) {
    var cachedValue: Float = 0f

    open fun compute(composable: Composable?, yaxis: Boolean) {
        cachedValue = updateCache(composable, yaxis)
    }

    /**
     * Updates [cachedValue] based on the given value.
     */
    abstract fun updateCache(composable: Composable?, yaxis: Boolean): Float
}