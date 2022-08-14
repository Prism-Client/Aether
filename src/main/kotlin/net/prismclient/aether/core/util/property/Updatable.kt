package net.prismclient.aether.core.util.property

import net.prismclient.aether.ui.composition.Composable

/**
 * [Updatable] is an interface which provides an update function that expects a nullable [Composable].
 *
 * @authors sen
 * @since 1.0
 * @see Property
 */
interface Updatable {
    /**
     * Invoked when the composition is updated.
     */
    fun compose(composable: Composable?)
}