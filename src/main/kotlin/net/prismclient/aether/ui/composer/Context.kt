package net.prismclient.aether.ui.composer

import net.prismclient.aether.ui.composition.Composable

object Context {
    @JvmStatic
    fun createContext(target: Composable): ComposableContext = ComposableContext.apply { updateContext(target) }
}