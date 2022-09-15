package net.prismclient.aether.ui.composer

import net.prismclient.aether.ui.composition.Composable

/**
 * Represents the active context of a [Composable].
 *
 * @author sen
 * @since 1.0
 */
object ComposableContext{
    var composable: Composable? = null

    fun updateContext(target: Composable) {
        composable = target
    }

    /**
     * Returns the [composable] and throws an exception if it is null.
     */
    fun activeComposable(): Composable = composable ?: throw IllegalStateException("There is no active composable")
}