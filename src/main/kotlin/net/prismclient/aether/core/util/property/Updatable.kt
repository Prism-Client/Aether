package net.prismclient.aether.core.util.property

import net.prismclient.aether.ui.composition.Composable

/**
 * [Updatable] is an interface which provides an update function that expects a nullable [T] which is
 * a [Composable] or a subclass of it.
 *
 * @authors sen
 * @since 1.0
 * @see UIProperty
 */
interface Updatable<T : Composable> {
    /**
     * Invoked when the composition is updated.
     */
    fun compose(composable: T?)
}