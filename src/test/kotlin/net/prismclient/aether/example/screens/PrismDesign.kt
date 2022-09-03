package net.prismclient.aether.example.screens

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Renderer.fontBounds
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.*
import net.prismclient.aether.ui.component.type.DefaultConstruct
import net.prismclient.aether.ui.component.type.IconModifier
import net.prismclient.aether.ui.component.type.imageColor
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.disableOptimizations
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.layout.AutoLayoutStyle
import net.prismclient.aether.ui.layout.HugLayout
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.scroll.Scrollbar
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.resource.ResourceProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.unit.other.Padding

object PrismDesign : UIScreen {
    @JvmStatic
    fun main(args: Array<String>) {
        Runner(PrismDesign)
    }

    var activeSidebarButton: DefaultConstruct? = null

    override fun createScreen() {
        val regular = "/fonts/Montserrat/Montserrat-Regular.ttf".toByteBuffer()
        val medium = "/fonts/Montserrat/Montserrat-Medium.ttf".toByteBuffer()
        Aether.renderer.createFont("Montserrat-Regular", regular)
        ResourceProvider.registerFont("Montserrat-Regular", regular)
        Aether.renderer.createFont("Montserrat-Medium", medium)
        ResourceProvider.registerFont("Montserrat-Medium", medium)

        ImageProvider.createSVG("frame", "/icons/vuesax/solid/frame.svg".toByteBuffer())
        ImageProvider.createSVG("home", "/icons/vuesax/custom/home.svg".toByteBuffer())
        ImageProvider.createSVG("setting", "/icons/vuesax/custom/setting.svg".toByteBuffer())
        ImageProvider.createSVG("folder", "/icons/vuesax/custom/folder.svg".toByteBuffer())
        ImageProvider.createSVG("profile", "/icons/vuesax/custom/profile.svg".toByteBuffer())
        ImageProvider.createSVG("shop", "/icons/vuesax/custom/shop.svg".toByteBuffer())


        ImageProvider.createSVG("messages", "/icons/vuesax/custom/messages.svg".toByteBuffer())
        ImageProvider.createSVG("friends", "/icons/vuesax/custom/friends.svg".toByteBuffer())
        ImageProvider.createSVG("achievements", "/icons/vuesax/custom/achievements.svg".toByteBuffer())
        ImageProvider.createSVG("recordings", "/icons/vuesax/custom/recordings.svg".toByteBuffer())

        compose(
            name = "Test",
            modifier = CompositionModifier()
                .size(1.rel, 1.rel)
                .disableOptimizations()
        ) {
            Background()
            PrismLogo()

            Column(
                modifier = LayoutModifier()
                    .position(48, 138),
                layoutStyle = AutoLayoutStyle()
                    .padding(Padding(14.px, 0.px, 0.px, 0.px))
                    .spacing(9.px)
            ) {
                modifier.height = modifier.height!!.atMost(0.6.rel - 48.px)
                modifier.verticalScrollbar = Scrollbar()
                modifier.optimizeComposition = true

                SideTitle("MENU")

                Column {
                    SideButton("home", "Dashboard")
                    SideButton("folder", "Mods")
                    SideButton("setting", "Settings")
                    SideButton("shop", "Store")
                    SideButton("profile", "Profiles")
                }

                SideTitle("SOCIAL")

                Column {
                    SideButton("home", "Messages")
                    SideButton("friends", "Friends")
                    SideButton("achievements", "Achievements")
                    SideButton("recordings", "Recordings")
                }
            }
        }
    }

    fun SideButton(iconName: String, buttonText: String) = AutoLayout(
        modifier = LayoutModifier().size(206.px, HugLayout()),
        layoutStyle = AutoLayoutStyle()
            .layoutDirection(LayoutDirection.HORIZONTAL)
            .layoutAlignment(Alignment.MIDDLELEFT)
            .layoutPadding(Padding(8.px, 9.px, 8.px, 9.px))
            .layoutSpacing(24.px)
    ) {
        modifier.clipContent = false
        modifier.optimizeComposition = false

        Icon(
            imageName = iconName, modifier = IconModifier().size(24, 24).imageColor((-1).rgb)
        )

        Button(
            text = buttonText,
            fontStyle = FontStyle().fontName("Montserrat-Regular").fontSize(14.px).fontColor(0x697483.rgb)
                .fontType(FontType.AutoWidth)
        )
    }

    fun SideTitle(text: String) = Button(
        text = text,
        modifier = Modifier(),
        fontStyle = FontStyle().fontName("Montserrat-Medium").fontSize(11.px).fontColor(0x697483.rgb)
            .fontType(FontType.AutoWidth)
    )

    fun Background() = construct {
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

    fun PrismLogo() = construct {
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