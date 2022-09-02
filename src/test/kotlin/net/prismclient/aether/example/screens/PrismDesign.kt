package net.prismclient.aether.example.screens

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Renderer.fontBounds
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.*
import net.prismclient.aether.ui.component.type.IconModifier
import net.prismclient.aether.ui.component.type.imageColor
import net.prismclient.aether.ui.composition.DefaultCompositionModifier
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.layout.HugLayout
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.hug
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.resource.ResourceProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.unit.other.Padding

class PrismDesign : UIScreen {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Runner(PrismDesign())
        }
    }

    override fun createScreen() {
        val regular = "/fonts/Montserrat/Montserrat-Regular.ttf".toByteBuffer()
        val medium = "/fonts/Montserrat/Montserrat-Medium.ttf".toByteBuffer()
        Aether.renderer.createFont("Montserrat-Regular", regular)
        ResourceProvider.registerFont("Montserrat-Regular", regular)
        Aether.renderer.createFont("Montserrat-Medium", medium)
        ResourceProvider.registerFont("Montserrat-Medium", medium)

        ImageProvider.createSVG("frame", "/icons/vuesax/solid/frame.svg".toByteBuffer())
        ImageProvider.createSVG("home", "/icons/vuesax/custom/home.svg".toByteBuffer())
        ImageProvider.createSVG("folder", "/icons/vuesax/custom/folder.svg".toByteBuffer())
        ImageProvider.createSVG("profile", "/icons/vuesax/custom/profile.svg".toByteBuffer())
        ImageProvider.createSVG("shop", "/icons/vuesax/custom/shop.svg".toByteBuffer())

        compose(
            name = "Test", modifier = DefaultCompositionModifier().size(1.rel, 1.rel)//.position(150.px, 50.px)
        ) {
//            this.modifier.optimizeComposition = false
            constructBackground()
            prismLogo().modifier.position(49, 52)

//            elementLabel("Hiiii")


            autoLayout(
                modifier = LayoutModifier()
                    .backgroundColor(RGBA(1f, 0f, 0f, 0.3f).rgba)
                    .position(48, 138)//.hugWidth().height(HugLayout().atLeast(200.px))
                    .hug()
                    //.height(HugLayout().atMost(200.px))
            ) {
                layoutDirection = LayoutDirection.VERTICAL
                layoutAlignment = UIAlignment.TOPLEFT
                layoutPadding = Padding(14.px, 0.px, 0.px, 0.px)
                itemSpacing = 9.px
                modifier.optimizeComposition = false
                modifier.clipContent = true

                elementLabel("MENU")

                // TODO: Unit.max(value).min(value)

                autoLayout(
                    modifier = LayoutModifier().hug()
                ) {
//                    modifier.optimizeComposition = false
                    layoutDirection = LayoutDirection.VERTICAL
                    layoutAlignment = UIAlignment.TOPLEFT
                    itemSpacing = 8.px
                    elementButton("home", "Dashboard")
                    elementButton("folder", "Mods")
                    elementButton("home", "Settings")
                    elementButton("shop", "Store")
                    elementButton("profile", "Profiles")
                }

                elementLabel("SOCIAL")

                autoLayout(
                    modifier = LayoutModifier().hug()
                ) {
                    modifier.clipContent = false
                    layoutDirection = LayoutDirection.VERTICAL
                    layoutAlignment = UIAlignment.TOPLEFT
                    itemSpacing = 8.px
                    elementButton("home", "Dashboard")
                    elementButton("folder", "Mods")
                    elementButton("home", "Settings")
                    elementButton("shop", "Store")
                    elementButton("profile", "Profiles")
                }
            }
        }
    }

    fun elementButton(icon: String, buttonText: String) = autoLayout(
        modifier = LayoutModifier().size(206.px, HugLayout())
    ) {
        modifier.clipContent = false
        layoutDirection = LayoutDirection.HORIZONTAL
        layoutAlignment = UIAlignment.MIDDLELEFT
        layoutPadding = Padding(8.px, 9.px, 8.px, 9.px)
        itemSpacing = 24.px

        icon(
            imageName = icon, modifier = IconModifier().size(24, 24).imageColor((-1).rgb)
        )

        button(
            text = buttonText,
            fontStyle = FontStyle().fontName("Montserrat-Regular").fontSize(14.px).fontColor(0x697483.rgb)
                .fontType(FontType.AutoWidth)
        )
    }

    fun elementLabel(text: String) = button(
        text = text,
        modifier = Modifier(),
        fontStyle = FontStyle().fontName("Montserrat-Medium").fontSize(11.px).fontColor(0x697483.rgb)
            .fontType(FontType.AutoWidth)
    )

    fun constructBackground() = construct {
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

    fun prismLogo() = construct {
        val logo = ImageProvider.createImage("PrismLogo", 0, "/icons/prismclient/PrismLogo_x128.png".toByteBuffer())
        val handle = logo.retrieveImage(34f, 37f) // todo: times device pixel ratio (retina device)
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
}