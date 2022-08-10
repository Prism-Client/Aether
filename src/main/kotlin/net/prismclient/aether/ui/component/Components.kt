package net.prismclient.aether.ui.component

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.util.other.ComposableGroup
import net.prismclient.aether.ui.util.shorthands.Block

var activeComposition: Composition? = null
var activeComposable: Composable? = null

/**
 * TODO: doc
 */
inline fun <T : Composable> component(composable: T, block: Block<T>): T {
    activeComposition = activeComposition ?: Aether.instance.defaultComposition
    composable.composition = activeComposition!!
    composable.parent = activeComposable ?: activeComposition!!
    if (composable.parent is ComposableGroup) {
        (composable.parent as ComposableGroup).children.add(composable)
    }
    val previousActiveComponent = activeComposable
    activeComposable = composable
    composable.block()
    activeComposable = previousActiveComponent
    return composable
}

/**
 * Creates a new composition with the given [name], and applies the [block].
 */
inline fun compose(name: String, modifier: CompositionModifier = CompositionModifier(), block: Block<Composition>): Composition {
    val composition = Aether.instance.createComposition(name, modifier)
    activeComposition = composition
    composition.block()
    activeComposition = null
    return composition
}

@JvmOverloads
inline fun button(
    text: String,
    modifier: UIModifier<*> = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    block: Block<UIButton> = {}
): UIButton = component(UIButton(text, modifier, fontStyle), block)