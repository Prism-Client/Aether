package net.prismclient.aether.ui.component

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.component.type.Label

/**
 * Used to encapsulate the values
 */
object ComponentUtil {
    @JvmStatic
    var activeComposition: Composition? = null
    @JvmStatic
    var activeComposable: Composable? = null
}

/**
 * TODO: doc
 */
inline fun <T : Composable> component(composable: T, block: Block<T>): T {
    ComponentUtil.activeComposition =ComponentUtil.activeComposition!!// ?: Aether.instance.defaultComposition
    composable.composition = ComponentUtil.activeComposition!!
    composable.parent = ComponentUtil.activeComposable ?: ComponentUtil.activeComposition!!
    if (composable.parent is ComposableGroup) {
        (composable.parent as ComposableGroup).children.add(composable)
    }
    val previousActiveComponent = ComponentUtil.activeComposable
    ComponentUtil.activeComposable = composable
    composable.block()
    ComponentUtil.activeComposable = previousActiveComponent
    return composable
}

/**
 * Creates a new composition with the given [name], and applies the [block].
 */
inline fun compose(name: String, modifier: CompositionModifier = CompositionModifier(), block: Block<Composition>): Composition {
    val composition = Aether.instance.createComposition(name, modifier)
    ComponentUtil.activeComposition = composition
    composition.block()
    ComponentUtil.activeComposition = null
    return composition
}

inline fun button(
    text: String,
    modifier: UIModifier<*> = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    block: Block<UIButton> = {}
): UIButton = component(UIButton(text, modifier, fontStyle), block)

inline fun label(
    text: String,
    modifier: UIModifier<*> = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    block: Block<Label> = {}
): Label = component(Label(text, modifier, fontStyle), block)