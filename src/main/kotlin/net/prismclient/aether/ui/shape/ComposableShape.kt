package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.core.util.property.Updatable

/**
 * [ComposableShape] is a shape which constrains itself based on a composable, or a specific subclass of
 * one known as [T]. When composed, the provided composable is expected not to be null.
 *
 * @author sen
 * @since 1.0
 */
abstract class ComposableShape<T : Composable> : Shape(), Updatable<T> {
    /**
     * The x position of the composable.
     */
    var initialX: Float = 0f

    /**
     * The y position of the composable.
     */
    var initialY: Float = 0f

    override fun compose(composable: T?) {
        composable!!
        x?.compute(composable, false)
        y?.compute(composable, true)
        width?.compute(composable, false)
        height?.compute(composable, true)
        initialX = composable.x
        initialY = composable.y
    }

    protected open fun UIUnit<*>?.compute(composable: Composable, yaxis: Boolean) {
        this?.compute(composable, composable.width, composable.height, yaxis)
    }
}