package net.prismclient.aether.example.screens

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Renderer.fontBounds
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.*
import net.prismclient.aether.ui.component.type.Icon
import net.prismclient.aether.ui.component.type.IconModifier
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.component.type.imageColor
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.disableOptimizations
import net.prismclient.aether.ui.composition.onClick
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.layout.AutoLayout
import net.prismclient.aether.ui.layout.BoxLayoutStyle
import net.prismclient.aether.ui.layout.HugLayout
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.scroll.Scrollbar
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.resource.ResourceProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.unit.other.Padding
import net.prismclient.aether.ui.unit.other.UIRadius
import java.io.File

object PrismDesign : UIScreen {
    @JvmStatic
    fun main(args: Array<String>) {
        Runner(PrismDesign)
    }

    lateinit var activeSidebarButton: AutoLayout

    override fun createScreen() {
        val regular = "/fonts/Montserrat/Montserrat-Regular.ttf".toByteBuffer()
        val medium = "/fonts/Montserrat/Montserrat-Medium.ttf".toByteBuffer()
        val poppinsMedium = "/fonts/Poppins/Poppins-Medium.ttf".toByteBuffer()
        Aether.renderer.createFont("Montserrat-Regular", regular)
        ResourceProvider.registerFont("Montserrat-Regular", regular)
        Aether.renderer.createFont("Montserrat-Medium", medium)
        ResourceProvider.registerFont("Montserrat-Medium", medium)
        Aether.renderer.createFont("Poppins-Medium", poppinsMedium)
        ResourceProvider.registerFont("Poppins-Medium", poppinsMedium)

        UIAssetDSL.heapLoad(
            location = File(PrismDesign::class.java.getResource("/icons/vuesax/custom")!!.toURI()),
            fileExtensions = arrayOf("svg"),
        ) { fileName, _, fileData -> ImageProvider.createSVG(fileName, fileData) }

        UIAssetDSL.heapLoad(
            location = File(PrismDesign::class.java.getResource("/icons/vuesax/solid")!!.toURI()),
            prefix = "solid/",
            fileExtensions = arrayOf("svg"),
        ) { fileName, _, fileData -> ImageProvider.createSVG(fileName, fileData) }

        UIAssetDSL.heapLoad(
            location = File(PrismDesign::class.java.getResource("/icons/vuesax/outline")!!.toURI()),
            prefix = "outline/",
            fileExtensions = arrayOf("svg"),
        ) { fileName, _, fileData -> ImageProvider.createSVG(fileName, fileData) }

        fun Pane(title: String) {
            Compose(
                name = "$title-pane",
                modifier = CompositionModifier()
                    .backgroundColor(RGBA(1f, 0f, 0f, 0.1f).rgba)
                    .constrain(253.px, 21.px, 1.rel - 253.px - 21.px, 1.rel - 42.px)
            ) {
                // Title
//                Button(
//                    text = title,
//                    modifier = Modifier()
//                        .position(47, 29),
//                    fontStyle = FontStyle()
//                        .fontName("Poppins-Medium")
//                        .fontColor(0x292D32.rgb)
//                        .fontSize(24.px)
//                        .fontType(FontType.AutoWidth)
//                )

                fun Module(name: String, iconName: String, enabled: Boolean, favorited: Boolean) = Box {
                    modifier
                        .size(230, 180)
                        .backgroundColor(0xF4F9FF.rgb)
                        .backgroundRadius(UIRadius(topLeft = 15.px, topRight = 15.px))

                    modifier.background!!.height = 1.crel - 39.px

//                    val icon = Image(
//                        imageName = iconName,
//                        modifier = IconModifier()
//                            .control(Alignment.CENTER)
//                            .anchor(Alignment.TOPCENTER)
//                            .y(36.px)
//                            .size(69, 69)
//                            .imageColor(0x292D32.rgb)
//                    )

                    Box(
                        modifier = LayoutModifier()
                            .backgroundColor(0x292D32.rgb)
                            .backgroundRadius(UIRadius(bottomLeft = 15.px, bottomRight = 15.px))
                    ) {
                        modifier
                            .size(1.rel, 39.px)
                            .control(Alignment.BOTTOMLEFT)
                        Button(
                            text = name,
                            modifier = Modifier()
                                .control(Alignment.MIDDLELEFT)
                                .x(15.px)
                                .backgroundColor(RGBA(0f, 1f, 0f, 0.1f).rgba),
                            fontStyle = FontStyle()
                                .fontName("Montserrat-Medium")
                                .fontColor(0xF4F9FF.rgb)
                                .fontSize(15.px)
                                .fontType(FontType.AutoWidth)
                                .fontAlignment(UITextAlignment.LEFT, UITextAlignment.CENTER)
                        )
                        Icon(
                            imageName = "grey/folder", //(if (favorited) "outline" else "solid") + "/star",
                            modifier = IconModifier()
                                .control(Alignment.BOTTOMRIGHT)
                                .size(16, 16)
                                .imageColor(0xFFFFFF.rgb)
                        )
                    }

//                    val btn = Button(
//                        text = name,
//                        modifier = Modifier()
//                            .size(1.rel, 39.px)
//                            .control(Alignment.BOTTOMLEFT)
//                            .backgroundColor(0x292D32.rgb)
//                            .backgroundRadius(UIRadius(bottomLeft = 15.px, bottomRight = 15.px)),
//                        fontStyle = FontStyle()
//                            .fontName("Montserrat-Medium")
//                            .fontSize(15.px)
//                            .fontColor(0xF4F9FF.rgb)
//                            .position(18.px, 0.5.crel - 7.px)
//                            .width(1.crel)
//                            .fontAlignment(UITextAlignment.LEFT, UITextAlignment.CENTER)
//                    )
//                    Icon(
//                        imageName = (if (favorited) "outline" else "solid") + "/star",
//                        modifier = IconModifier()
//                            .size(16, 16)
//                            .imageColor(0xFFFFFF.rgb)
//                    )

//                    if (enabled) {
//                        btn.modifier.backgroundColor(0x1471EB.rgb)
//                    }
                }

                Module("CPS", "solid/mouse", false, true)
            }
        }

        Pane("Mods")


        Compose(
            name = "Test",
            modifier = CompositionModifier()
                .size(1.rel, 1.rel)
                .disableOptimizations()
        ) {
            Background()
            PrismLogo()

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
                    activeSidebarButton = SideButton("gradient/home", "Dashboard")
                    (activeSidebarButton.children[1] as UIButton).fontStyle.fontColor(0x292D32.rgb)
                    SideButton("grey/folder", "Mods")
                    SideButton("grey/setting", "Settings")
                    SideButton("grey/shop", "Store")
                    SideButton("grey/profile", "Profiles")
                }

                SideTitle("SOCIAL")

                Column(name = "layout2") {
                    SideButton("grey/messages", "Messages")
                    SideButton("grey/friends", "Friends")
                    SideButton("grey/recordings", "Recordings")
                }
            }
        }
    }

    fun SideButton(iconName: String, buttonText: String) = AutoLayout(
        name = buttonText,
        modifier = LayoutModifier().size(206.px, HugLayout()),
        layoutStyle = BoxLayoutStyle()
            .layoutDirection(LayoutDirection.HORIZONTAL)
            .layoutAlignment(Alignment.MIDDLELEFT)
            .layoutPadding(Padding(8.px, 9.px, 8.px, 9.px))
            .layoutSpacing(24.px)
    ) {
        modifier.clipContent = false
        modifier.optimizeComposition = false

        val image = Image(
            imageName = iconName, modifier = IconModifier().size(24, 24).imageColor((-1).rgb)
        )

        val button = Button(
            text = buttonText,
            fontStyle = FontStyle().fontName("Montserrat-Regular").fontSize(14.px).fontColor(0x697483.rgb)
                .fontType(FontType.AutoWidth)
        )

        onClick {
            val icn = activeSidebarButton.children[0] as Icon
            icn.image = ImageProvider.obtainImage(icn.image.imageName.replace("gradient", "grey"))!!

            val btn = activeSidebarButton.children[1] as UIButton
            btn.fontStyle.fontColor(0x697483.rgb)

            activeSidebarButton = this
            image.image = ImageProvider.obtainImage(image.image.imageName.replace("grey", "gradient"))!!
            button.fontStyle.fontColor(0x292D32.rgb)
        }
    }

    fun SideTitle(text: String) = Button(
        text = text,
        modifier = Modifier(),
        fontStyle = FontStyle().fontName("Montserrat-Medium").fontSize(11.px).fontColor(0x697483.rgb)
            .fontType(FontType.AutoWidth)
    )

    fun Background() = Construct {
        it.modifier.size(1.rel, 1.rel)
        render {
            path {
                hole {
                    rect(it.x, it.y, it.width, it.height)
                    rect(it.x + 253, it.y + 21f, it.width - 253 - 21, it.height - 21f - 21f, 35f)
                }
            }.fillPath(RGBA(1f, 1f, 1f, 1f))
        }
    }

    fun PrismLogo() = Construct {
        val logo = ImageProvider.createImage("PrismLogo", 0, "/icons/prismclient/PrismLogo_x128.png".toByteBuffer())
        val handle = logo.retrieveImage(34f, 37f)
        it.modifier.position(49, 52)
        it.modifier.size(145, 37)
        render {
            color(-1)
            renderImage(handle, it.x, it.y, 34f, 37f)
            color(RGBA(41, 45, 50))
            font("Montserrat-Medium", 24f, UITextAlignment.LEFT, UITextAlignment.TOP, 1.025f)
            "PRISM".render(it.x + 46, it.y + 1 + 5)

            color(RGBA(105, 116, 131))
            font("Montserrat-Regular", 8f, UITextAlignment.RIGHT, UITextAlignment.TOP, 0f)
            "Aether-v1.0.0".render(fontBounds()[2], it.y + 27f + 5f)
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
}