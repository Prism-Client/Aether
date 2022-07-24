package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.UIComposition

typealias Block<T> = T.() -> Unit

/**
 * Creates a new composition with the given [name], and applies the [block]. The composition is automatically
 * closed after the block is executed, so [Aether.activeComposition] will be null once the block is completed.
 */
inline fun compose(name: String, block: Block<UIComposition>): UIComposition {
    if (Aether.instance.activeComposition != null)
        throw RuntimeException("Failed to create composition as there is already an active one.")
    val composition = Aether.instance.createComposition(name)
    Aether.instance.activeComposition = composition
    composition.block()
    Aether.instance.activeComposition = null
    return composition
}