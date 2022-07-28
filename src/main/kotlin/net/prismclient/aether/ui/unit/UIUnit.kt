package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.util.other.Copyable

/**
 * [UIUnit] is the superclass of all units, which represent an amount of pixels on the screen when
 * computed. It is used to describe the position, and size of components and shapes as well as other
 * pixel based properties. Some layouts might adjust to the components within it, so if the unit
 * changes based on the parent composition, the composable must be set to [Composable.dynamic] for
 * Aether to know when to re-update the layout.
 *
 * The provided generic value must be the class which is inheriting this class. It is used for the interface
 * [Copyable] so that the unit can be copied to be manipulated with later.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIUnit<T : UIUnit<T>>(open var value: Float) : Copyable<T> {
    open var cachedValue: Float = 0f

    open fun compute(composable: Composable?, yaxis: Boolean) {
        cachedValue = updateCache(composable, yaxis)
    }

    /**
     * Updates [cachedValue] based on the given value.
     */
    abstract fun updateCache(composable: Composable?, yaxis: Boolean): Float
}