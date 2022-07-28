package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.composition.Composable

/**
 * [ComposableShape] is a shape which constrains itself based on a composition.
 *
 * @author sen
 * @since 1.0
 */
abstract class ComposableShape : Shape() {
    open fun update(composable: Composable) {
        x?.compute(composable, false)
        y?.compute(composable, true)
        width?.compute(composable, false)
        height?.compute(composable, true)
    }
}