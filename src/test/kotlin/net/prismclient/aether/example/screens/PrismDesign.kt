package net.prismclient.aether.example.screens

import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Renderer.fontBounds
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.*
import net.prismclient.aether.ui.component.type.ImageComponent
import net.prismclient.aether.ui.component.type.IconModifier
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.component.type.imageColor
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.disableOptimizations
import net.prismclient.aether.ui.composition.onClick
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.Compose
import net.prismclient.aether.ui.dsl.ComposeDSL
import net.prismclient.aether.ui.dsl.Resource
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.layout.AutoLayout
import net.prismclient.aether.ui.layout.BoxLayoutStyle
import net.prismclient.aether.ui.layout.HugLayout
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.scroll.Scrollbar
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.renderer.UIStrokeDirection
import net.prismclient.aether.ui.screen.CloseableScreen
import net.prismclient.aether.ui.unit.other.Padding
import kotlin.random.Random

object PrismDesign : CloseableScreen {
    var initialized = false

    lateinit var activePane: Composition

    @JvmStatic
    fun main(args: Array<String>) {
        Runner(PrismDesign)
    }

    lateinit var activeSidebarButton: AutoLayout

    override fun compose() = Compose {
        if (!initialized) {
            Resource {
                fontCollection(localResource("/fonts/Poppins"))
                fontCollection(localResource("/fonts/Montserrat"))

                pngCollection(localResource("/images/"))
                svgCollection(localResource("/icons/"))
            }
            initialized = true
        }

        Composition(
            name = "Test",
            modifier = CompositionModifier()
                .size(1.rel, 1.rel)
                .disableOptimizations()
        ) {
            Background()
            PrismLogo()

            activePane = Composition(
                name = "Viewport",
                modifier = CompositionModifier()
                    .constrain(253.px, 21.px, 1.rel - 253.px - 21.px, 1.rel - 42.px),
            )

            Dashboard()

            Column(
                name = "Main Column",
                modifier = LayoutModifier()
                    .position(48, 138),
                layoutStyle = BoxLayoutStyle()
                    .padding(Padding(14.px, 0.px, 0.px, 0.px))
                    .spacing(9.px)
            ) {
                modifier.height = modifier.height!!.atMost(0.6.rel - 48.px)
                modifier.verticalScrollbar = Scrollbar()
                modifier.optimizeComposition = true

                SideTitle("MENU")

                Column(name = "layout1") {
                    modifier.height(HugLayout() + 30.px)
                    activeSidebarButton = SideButton("gradient/home", "Dashboard")
                    (activeSidebarButton.children[1] as UIButton).fontStyle.fontColor(0x292D32.rgb)
                    SideButton("solid/folder", "Mods")
                    SideButton("solid/settings", "Settings")
                    SideButton("solid/bag", "Store")
                    SideButton("solid/profile", "Profiles")
                }

                SideTitle("SOCIAL")

                Column(name = "layout2") {
                    SideButton("solid/messages", "Messages")
                    SideButton("solid/people", "Friends")
                    SideButton("solid/video", "Recordings")
                }
            }

            val radius = 16f

            Row(
                modifier = LayoutModifier()
                    .position(48.px, 1.rel - 51.px)
                    .anchor(Alignment.BOTTOMLEFT),
                layoutStyle = BoxLayoutStyle()
                    .spacing(20.px)
            ) {
                layoutStyle.alignment(Alignment.MIDDLELEFT)
                Construct(
                    modifier = Modifier()
                        .size(32, 32)
                        .backgroundRadius(radius.radius)
                        .border(0x57FF72.rgb, 1.px, UIStrokeDirection.INSIDE)
                        .padding(1.padding)
                ) {
                    Render {
                        color(-1)
                        renderImage("Character", it.x, it.y, it.width, it.height, radius, radius, radius, radius)
                    }
                }
                Label(
                    text = "Username",
                    fontStyle = FontStyle()
                        .fontName("Poppins-Medium")
                        .fontSize(14.px)
                        .fontColor(0x292D32.rgb)
                        .fontType(FontType.AutoWidth)
                )
            }
        }
    }

    fun SideButton(iconName: String, buttonText: String) = AutoLayout(
        name = buttonText,
        modifier = LayoutModifier()
            .size(206.px, HugLayout())
            .disableOptimizations(),
        layoutStyle = BoxLayoutStyle()
            .layoutDirection(LayoutDirection.HORIZONTAL)
            .layoutAlignment(Alignment.MIDDLELEFT)
            .layoutPadding(Padding(8.px, 9.px, 8.px, 9.px))
            .layoutSpacing(24.px)
    ) {
        modifier.clipContent = false

        val image = Image(
            imageName = iconName,
            modifier = IconModifier()
                .size(24, 24)
                .imageColor((-1).rgb)
        )

        val button = Button(
            text = buttonText,
            fontStyle = FontStyle().fontName("Poppins-Regular").fontSize(14.px).fontColor(0x697483.rgb)
                .fontType(FontType.AutoWidth)
        )

        onClick {
            val icn = activeSidebarButton.children[0] as ImageComponent
            icn.image = ImageProvider.obtainImage(icn.image.imageName.replace("gradient", "solid"))!!

            val btn = activeSidebarButton.children[1] as UIButton
            btn.fontStyle.fontColor(0x697483.rgb)

            activeSidebarButton = this
            image.image = ImageProvider.obtainImage(image.image.imageName.replace("solid", "gradient"))!!
            button.fontStyle.fontColor(0x292D32.rgb)
        }
    }

    fun SideTitle(text: String) = Button(
        text = text,
        modifier = Modifier(),
        fontStyle = FontStyle()
            .x(8.px)
            .fontName("Poppins-Medium")
            .fontSize(11.px)
            .fontColor(0x697483.rgb)
            .fontType(FontType.AutoWidth)
    )

    fun Background() = Construct {
        it.modifier.size(1.rel, 1.rel)
        Render {
            path {
                hole {
                    rect(it.x, it.y, it.width, it.height)
                    rect(it.x + 253, it.y + 21f, it.width - 253 - 21, it.height - 21f - 21f, 35f)
                }
            }.fillPath(RGBA(1f, 1f, 1f, 1f))
        }
    }

    fun PrismLogo() = Construct {
        val handle = ImageProvider.obtainImage("Prism-Logo")!!.retrieveImage(34f, 47f)
        it.modifier.position(48 + 8, 52)
        it.modifier.size(145, 37)
        Render {
            color(-1)
            renderImage(handle, it.x, it.y, 34f, 37f)
            color(RGBA(41, 45, 50))
            font("Montserrat-Medium", 24f, UITextAlignment.LEFT, UITextAlignment.TOP, 1.025f)
            "PRISM".render(it.x + 46, it.y + 1 + 5)

            color(RGBA(105, 116, 131))
            font("Montserrat-Regular", 8f, UITextAlignment.RIGHT, UITextAlignment.TOP, 0f)
            "v1.0.0-Beta".render(fontBounds()[2], it.y + 27f + 5f)
        }
    }

    fun Scrollbar() = Scrollbar {
        x = 1.crel - 10.px
        y = 0.025.crel
        width = 5.px
        height = 0.95.crel
        thumbColor = 0x434343.rgb
        thumbRadius = 2.5.radius

        val scrollbarBackground = UIBackground()

        scrollbarBackground.backgroundColor = RGBA(0f, 0f, 0f, 0.15f).rgba
        scrollbarBackground.backgroundRadius = 2.5.radius

        background = scrollbarBackground
    }

    inline fun Pane(block: Block<Composition>) {
        activePane.children.clear()
        Compose {
            val prev1 = activeComposable
            val prev2 = activeComposition

            activeComposable = activePane
            activeComposition = activePane

            block(activePane)

            activeComposable = prev1
            activeComposition = prev2
        }
    }

    override fun closeScreen() {

    }
}