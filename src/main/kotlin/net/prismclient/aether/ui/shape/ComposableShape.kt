package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.Updatable

/**
 * [ComposableShape] is a shape which constrains itself based on a composition. When [ComposableShape.update]
 * is invoked, the expected [Composable] cannot be null, else a NullPointerException will be thrown.
 *
 * @author sen
 * @since 1.0
 */
abstract class ComposableShape : Shape(), Updatable {
    /**
     * The x position of the composable.
     */
    var initialX: Float = 0f

    /**
     * The y position of the composable.
     */
    var initialY: Float = 0f

    override fun update(composable: Composable?) {
        composable!!
        x?.compute(composable, false)
        y?.compute(composable, true)
        width?.compute(composable, false)
        height?.compute(composable, true)
        initialX = if (composable is Composition) 0f else composable.x
        initialY = if (composable is Composition) 0f else composable.y
    }

    @Suppress
    protected fun UIUnit<*>?.compute(composable: Composable, yaxis: Boolean) {
        this?.compute(composable, composable.width, composable.height, yaxis)
    }
}