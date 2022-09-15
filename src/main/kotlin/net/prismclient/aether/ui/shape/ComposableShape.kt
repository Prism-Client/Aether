package net.prismclient.aether.ui.shape

import net.prismclient.aether.core.util.property.Updatable
import net.prismclient.aether.ui.composer.ComposableContext
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.compute

/**
 * [ComposableShape] is a shape which constrains itself based on a composable, or a specific subclass of
 * one known as [T]. When composed, the provided composable is expected not to be null.
 *
 * @author sen
 * @since 1.0
 */
abstract class ComposableShape<T : Composable> : Shape(), Updatable {
    /**
     * The x position of the composable.
     */
    var initialX: Float = 0f

    /**
     * The y position of the composable.
     */
    var initialY: Float = 0f

    override fun compose(context: ComposableContext) {
        x.compute(context, false)
        y.compute(context, true)
        width.compute(context, false)
        height.compute(context, true)
        initialX = context.activeComposable().x
        initialY = context.activeComposable().y
    }
}