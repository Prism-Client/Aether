package net.prismclient.aether.ui.component

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.util.shorthands.Block

var activeComposition: Composition? = null
var activeComponent: UIComponent<*>? = null

/**
 * Applies the given component to the active state by changing the parent and adding the component to the composition.
 */
inline fun <T : UIComponent<*>> component(component: T, block: Block<T>): T {
    activeComposition = activeComposition ?: Aether.instance.defaultComposition
    component.composition = activeComposition!!
    component.parent = activeComponent ?: activeComposition!!
    val previousActiveComponent = activeComponent
    activeComponent = component
    component.block()
    activeComponent = previousActiveComponent
    return component
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