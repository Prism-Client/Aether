package net.prismclient.aether.ui.component

import net.prismclient.aether.core.event.MousePress
import net.prismclient.aether.core.metrics.Size
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
import net.prismclient.aether.ui.dsl.ComposeDSL.composable
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

inline fun Button(
    text: String,
    modifier: UIModifier<*> = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    block: Block<UIButton> = {},
): UIButton = composable(UIButton(text, modifier, fontStyle), block)

inline fun Label(
    text: String,
    modifier: UIModifier<*> = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    noinline onClick: (UIButton.(MousePress) -> Unit)? = null,
    block: Block<UIButton> = {},
): UIButton = Button(text, modifier, fontStyle, block).apply {
    if (onClick != null) onClick { this.onClick(it) }
}

inline fun Image(
    imageName: UIImage,
    modifier: IconModifier = IconModifier(),
    block: Block<ImageComponent> = {},
): ImageComponent = composable(ImageComponent(imageName, modifier), block)

inline fun Image(
    imageName: String,
    modifier: IconModifier = IconModifier(),
    block: Block<ImageComponent> = {},
): ImageComponent = composable(ImageComponent(imageName, modifier), block)

inline fun Icon(
    imageName: String,
    modifier: IconModifier = IconModifier(),
    block: Block<ImageComponent> = {},
): ImageComponent = Image(imageName, modifier, block)

inline fun Composition(
    name: String = "",
    modifier: CompositionModifier<*> = CompositionModifier(),
    block: Block<Composition> = {}
): Composition = composable(net.prismclient.aether.ui.composition.Composition(name, modifier), block)

/**
 * Creates a new [DefaultConstruct], a composable which executes the [block] when rendered. An example
 * use case is creating a shape without using composables. [ConstructionDSL] is intended to be
 * used along with this function.
 *
 * @see ConstructionDSL
 */
inline fun Construct(
    modifier: UIModifier<*> = Modifier(),
    block: ConstructionDSL.(construct: DefaultConstruct) -> Unit = {},
) = composable(DefaultConstruct(modifier)) {
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
    block: Block<UIListLayout> = {},
) = composable(UIListLayout("Vertical List", direction, order, childSpacing, modifier), block)

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
    block: Block<UIListLayout> = {},
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
    block: Block<UIListLayout> = {},
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
    block: Block<BoxLayout> = {},
): BoxLayout = composable(
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
    block: Block<AutoLayout> = {},
): AutoLayout = composable(AutoLayout(name, modifier, layoutStyle), block)

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
    block: Block<AutoLayout> = {},
): AutoLayout = composable(
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
    block: Block<AutoLayout> = {},
): AutoLayout = composable( // TODO: Modified State checking
    AutoLayout(
        name = name,
        modifier = modifier.hug().disableOptimizations(),
        layoutStyle = layoutStyle
            .layoutAlignment(horizontalConvert(horizontalAlignment))
            .layoutDirection(LayoutDirection.VERTICAL)
    ), block
)

inline fun Layout(
    name: String = "CustomLayout",
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: BoxLayoutStyle = BoxLayoutStyle(),
    noinline sizeCalculation: CustomLayout.() -> Size = { Size(0f, 0f) },
    noinline unitCalculation: CustomLayout.(layoutSize: Size?) -> Unit = {},
    noinline layout: CustomLayout.(children: ArrayList<Composable>, layoutSize: Size?) -> Size,
    block: Block<CustomLayout> = {}
): CustomLayout = composable(
    CustomLayout(name, modifier, layoutStyle, sizeCalculation, unitCalculation, layout),
    block
)