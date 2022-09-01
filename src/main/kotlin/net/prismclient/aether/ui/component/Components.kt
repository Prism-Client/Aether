package net.prismclient.aether.ui.component

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.component.type.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.dsl.ConstructionDSL
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.image.UIImage
import net.prismclient.aether.ui.layout.AutoLayout
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.UIListLayout
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.LayoutOrder
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit

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
inline fun <T : Composable> component(composable: T, block: Block<T> = {}): T {
    // TODO: Error when composition not foudn n stuff
    ComponentUtil.activeComposition = ComponentUtil.activeComposition!!// ?: Aether.instance.defaultComposition
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

inline fun icon(
        imageName: String,
        modifier: IconModifier = IconModifier(),
        block: Block<Icon> = {}
): Icon = component(Icon(imageName, modifier), block)

inline fun icon(
        image: UIImage,
        modifier: IconModifier = IconModifier(),
        block: Block<Icon> = {}
): Icon = component(Icon(image, modifier), block)

/**
 * Creates a new [DefaultConstruct], a component which executes the [block] when rendered. An example
 * use case is creating a shape without using components. [ConstructionDSL] is intended to be
 * used along with this function.
 *
 * @see ConstructionDSL
 */
inline fun construct(
        modifier: UIModifier<*> = Modifier(),
        block: ConstructionDSL.(construct: DefaultConstruct) -> Unit = {}
) = component(DefaultConstruct(modifier)) {
    val previousConstruct = ConstructionDSL.activeConstructor
    ConstructionDSL.activeConstructor = this
    ConstructionDSL.block(this)
    ConstructionDSL.activeConstructor = previousConstruct
}

/**
 * Creates a new list layout with the given [direction], [order], [childSpacing], [modifier] and
 * executes the [block] within the list layout's scope.
 *
 * @see horizontalList
 * @see verticalList
 */
inline fun listLayout(
        direction: LayoutDirection,
        order: LayoutOrder,
        childSpacing: UIUnit<*>?,
        modifier: LayoutModifier<*>,
        block: Block<UIListLayout> = {}
) = component(UIListLayout(direction, order, childSpacing, modifier), block)

/**
 * Creates a list layout with the [UIListLayout.direction] set to horizontal.
 *
 * @see listLayout
 * @see verticalList
 */
inline fun horizontalList(
        order: LayoutOrder = LayoutOrder.FIRST,
        childSpacing: UIUnit<*>? = null,
        modifier: LayoutModifier<*> = LayoutModifier(),
        block: Block<UIListLayout> = {}
) = listLayout(LayoutDirection.HORIZONTAL, order, childSpacing, modifier, block)

/**
* Creates a list layout with the [UIListLayout.direction] set to horizontal.
*
* @see listLayout
* @see horizontalList
*/
inline fun verticalList(
        order: LayoutOrder = LayoutOrder.FIRST,
        childSpacing: UIUnit<*>? = null,
        modifier: LayoutModifier<*> = LayoutModifier(),
        block: Block<UIListLayout> = {}
) = listLayout(LayoutDirection.VERTICAL, order, childSpacing, modifier, block)

inline fun autoLayout(modifier: LayoutModifier<*> = LayoutModifier(), block: Block<AutoLayout> = {}): AutoLayout =
        component(AutoLayout(modifier), block)

