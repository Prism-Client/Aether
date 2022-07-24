package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.component.UIComponent

/**
 * @author sen
 * @since 1.0
 */
abstract class UIUnit(var value: Float) {
    var cachedValue: Float = 0f

    open fun compute(component: UIComponent, yaxis: Boolean) {
        cachedValue = updateCache(component, yaxis)
    }

    /**
     * Updates [cachedValue] based on the given value.
     */
    abstract fun updateCache(component: UIComponent, yaxis: Boolean): Float
}