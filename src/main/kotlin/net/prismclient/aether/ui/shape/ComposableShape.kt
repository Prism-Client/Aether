package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.util.shorthands.dp

/**
 * [ComposableShape] is a shape which constrains itself based on a composition.
 *
 * @author sen
 * @since 1.0
 */
abstract class ComposableShape : Shape() {
    /**
     * The x position of the composable.
     */
    var initialX: Float = 0f

    /**
     * The y position of the composable.
     */
    var initialY: Float = 0f

    open fun update(composable: Composable) {
        x?.compute(composable, false)
        y?.compute(composable, true)
        width?.compute(composable, false)
        height?.compute(composable, true)
        initialX = if (composable is Composition) 0f else composable.x.dp
        initialY = if (composable is Composition) 0f else composable.y.dp
    }
}