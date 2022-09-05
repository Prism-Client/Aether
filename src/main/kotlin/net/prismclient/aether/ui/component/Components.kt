package net.prismclient.aether.ui.component

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.alignment.HorizontalAlignment
import net.prismclient.aether.ui.alignment.VerticalAlignment
import net.prismclient.aether.ui.alignment.horizontalConvert
import net.prismclient.aether.ui.alignment.verticalConvert
import net.prismclient.aether.ui.component.type.DefaultConstruct
import net.prismclient.aether.ui.component.type.IconModifier
import net.prismclient.aether.ui.component.type.ImageComponent
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.composition.*
import net.prismclient.aether.ui.dsl.ConstructionDSL
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.image.UIImage
import net.prismclient.aether.ui.layout.*
import net.prismclient.aether.ui.layout.AutoLayout
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
inline fun Compose(
    name: String,
    modifier: CompositionModifier<*> = DefaultCompositionModifier(),
    block: Block<Composition>
): Composition {
    val composition = Aether.instance.createComposition(name, modifier)
    val previousComposition = ComponentUtil.activeComposition
    ComponentUtil.activeComposition = composition
    composition.block()
    ComponentUtil.activeComposition = previousComposition
    return composition
}

inline fun Button(
    text: String,
    modifier: UIModifier<*> = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    block: Block<UIButton> = {}
): UIButton = component(UIButton(text, modifier, fontStyle), block)

inline fun Label(
    text: String,
    modifier: UIModifier<*> = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    block: Block<UIButton> = {}
): UIButton = Button(text, modifier, fontStyle, block)

inline fun Image(
    imageName: UIImage,
    modifier: IconModifier = IconModifier(),
    block: Block<ImageComponent> = {}
): ImageComponent = component(ImageComponent(imageName, modifier), block)

inline fun Image(
    imageName: String,
    modifier: IconModifier = IconModifier(),
    block: Block<ImageComponent> = {}
): ImageComponent = component(ImageComponent(imageName, modifier), block)

inline fun Icon(
    imageName: String,
    modifier: IconModifier = IconModifier(),
    block: Block<ImageComponent> = {}
): ImageComponent = Image(imageName, modifier, block)

/**
 * Creates a new [DefaultConstruct], a component which executes the [block] when rendered. An example
 * use case is creating a shape without using components. [ConstructionDSL] is intended to be
 * used along with this function.
 *
 * @see ConstructionDSL
 */
inline fun Construct(
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
 * @see HorizontalList
 * @see VerticalList
 */
inline fun ListLayout(
    direction: LayoutDirection,
    order: LayoutOrder,
    childSpacing: UIUnit<*>?,
    modifier: LayoutModifier<*>,
    block: Block<UIListLayout> = {}
) = component(UIListLayout("Vertical List", direction, order, childSpacing, modifier), block)

/**
 * Creates a list layout with the [UIListLayout.direction] set to horizontal.
 *
 * @see ListLayout
 * @see VerticalList
 */
inline fun HorizontalList(
    order: LayoutOrder = LayoutOrder.FIRST,
    childSpacing: UIUnit<*>? = null,
    modifier: LayoutModifier<*> = LayoutModifier(),
    block: Block<UIListLayout> = {}
) = ListLayout(LayoutDirection.HORIZONTAL, order, childSpacing, modifier, block)

/**
 * Creates a list layout with the [UIListLayout.direction] set to horizontal.
 *
 * @see ListLayout
 * @see HorizontalList
 */
inline fun VerticalList(
    order: LayoutOrder = LayoutOrder.FIRST,
    childSpacing: UIUnit<*>? = null,
    modifier: LayoutModifier<*> = LayoutModifier(),
    block: Block<UIListLayout> = {}
) = ListLayout(LayoutDirection.VERTICAL, order, childSpacing, modifier, block)


/**
 * Creates a Box Layout, which is one of the simplest layouts which does nothing with the
 * items within it. In can be considered a container to organize a group of [Composable]s.
 * The layout will automatically resize to the [Composable]s within.
 */
inline fun Box(
    name: String = "Box",
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: BoxLayoutStyle = BoxLayoutStyle(),
    block: Block<BoxLayout> = {}
): BoxLayout = component(
    BoxLayout(
        name = name,
        modifier = modifier.hug(),
        layoutStyle = layoutStyle
    ), block
)

/**
 * Creates an [AutoLayout] which is an advanced list style layout which mimics the behaviour
 * of Figma Auto Layouts.
 */
inline fun AutoLayout(
    name: String = "AutoLayout",
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: BoxLayoutStyle = BoxLayoutStyle(),
    block: Block<AutoLayout> = {}
): AutoLayout = component(AutoLayout(name, modifier, layoutStyle), block)

/**
 * Creates an [AutoLayout] with a row based layout style. Each [Composable] will be placed to the
 * right of the previous [Composable]. Any changes to the size of the [modifier], and the alignment,
 * and direction of the [layoutStyle] will be overwritten if set within the function parameters.
 * Furthermore, the layout is not using optimizations by default.
 */
inline fun Row(
    name: String = "Row",
    verticalAlignment: VerticalAlignment = VerticalAlignment.TOP,
    /* horizontalArrangement */
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: BoxLayoutStyle = BoxLayoutStyle(),
    block: Block<AutoLayout> = {}
): AutoLayout = component(
    AutoLayout(
        name = name,
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
 * Furthermore, the layout is not using optimizations by default.
 */
inline fun Column(
    name: String = "Column",
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.LEFT,
    /* verticalArrangement */
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: BoxLayoutStyle = BoxLayoutStyle(),
    block: Block<AutoLayout> = {}
): AutoLayout = component(
    AutoLayout(
        name = name,
        modifier = modifier.hug().disableOptimizations(),
        layoutStyle = layoutStyle
            .layoutAlignment(horizontalConvert(horizontalAlignment))
            .layoutDirection(LayoutDirection.VERTICAL)
    ), block
)