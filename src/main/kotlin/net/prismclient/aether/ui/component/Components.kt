package net.prismclient.aether.ui.component

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.alignment.HorizontalAlignment
import net.prismclient.aether.ui.alignment.VerticalAlignment
import net.prismclient.aether.ui.alignment.horizontalConvert
import net.prismclient.aether.ui.alignment.verticalConvert
import net.prismclient.aether.ui.component.type.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.DefaultCompositionModifier
import net.prismclient.aether.ui.dsl.ConstructionDSL
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.image.UIImage
import net.prismclient.aether.ui.layout.AutoLayout
import net.prismclient.aether.ui.layout.AutoLayoutStyle
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.UIListLayout
import net.prismclient.aether.ui.layout.hug
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
inline fun compose(
    name: String,
    modifier: CompositionModifier<*> = DefaultCompositionModifier(),
    block: Block<Composition>
): Composition {
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
) = component(UIListLayout("Vertical List", direction, order, childSpacing, modifier), block)

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

inline fun AutoLayout(
    layoutName: String = "AutoLayout",
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: AutoLayoutStyle = AutoLayoutStyle(),
    block: Block<AutoLayout> = {}
): AutoLayout = component(AutoLayout(layoutName, modifier, layoutStyle), block)

/**
 * Creates an [AutoLayout] with a row based layout style. Each [Composable] will be placed to the
 * right of the previous [Composable]. Any changes to the size of the [modifier], and the alignment,
 * and direction of the [layoutStyle] will be overwritten if set within the function parameters.
 * Furthermore, the layout is not using optimizations or clipping content by default.
 */
inline fun Row(
    verticalAlignment: VerticalAlignment = VerticalAlignment.TOP,
    /* horizontalArrangement */
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: AutoLayoutStyle = AutoLayoutStyle(),
    block: Block<AutoLayout> = {}
): AutoLayout = component(
    AutoLayout(
        layoutName = "Row",
        modifier = modifier.hug().disableOptimizations(),
        layoutStyle = layoutStyle
            .layoutAlignment(verticalConvert(verticalAlignment))
            .layoutDirection(LayoutDirection.HORIZONTAL)
    ), block
)

/**
 * Creates an [AutoLayout] with a column based layout style. Each [Composable] will be placed
 * below the previous [Composable]. Any changes to the size of the [modifier], and the alignment,
 * and direction of the [layoutStyle] will be overwritten if set within the function parameters.
 * Furthermore, the layout is not using optimizations or clipping content by default.
 */
inline fun Column(
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.LEFT,
    /* verticalArrangement */
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: AutoLayoutStyle = AutoLayoutStyle(),
    block: Block<AutoLayout> = {}
): AutoLayout = component(
    AutoLayout(
        layoutName = "Column",
        modifier = modifier.hug().disableOptimizations(),
        layoutStyle = layoutStyle
            .layoutAlignment(horizontalConvert(horizontalAlignment))
            .layoutDirection(LayoutDirection.VERTICAL)
    ), block
)