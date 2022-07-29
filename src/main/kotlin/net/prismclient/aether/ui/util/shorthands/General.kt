package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.modifier.Modifier

/**
 * Creates a new composition with the given [name], and applies the [block]. The composition is automatically
 * closed after the block is executed, so [Aether.activeComposition] will be null once the block is completed.
 */
inline fun compose(name: String, modifier: Modifier = Modifier(), block: Block<Composition>): Composition {
    val composition = Aether.instance.createComposition(name, modifier)
    Aether.instance.activeComposition = composition
    composition.block()
    Aether.instance.activeComposition = null
    return composition
}