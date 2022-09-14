package net.prismclient.aether.core.util.property

import net.prismclient.aether.ui.composer.ComposableContext
import net.prismclient.aether.ui.composition.Composable

/**
 *
 */
interface Updatable {
    /**
     * Invoked when the composition is updated.
     */
    fun compose(context: ComposableContext)
}