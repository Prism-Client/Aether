package net.prismclient.aether.example.screens

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.compose
import net.prismclient.aether.ui.component.construct
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.resource.ResourceProvider
import net.prismclient.aether.ui.screen.UIScreen

class TestScreen : UIScreen {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Runner(TestScreen())
        }
    }

    override fun createScreen() {

        val image = ImageProvider.createImage("PrismLogo", 0, "/icons/prismclient/PrismLogo_x128.png".toByteBuffer())
        val svg = ImageProvider.createSVG("svg", "/icons/vuesax/solid/check.svg".toByteBuffer())

        val regular = "/fonts/Montserrat/Montserrat-Regular.ttf".toByteBuffer()
        val medium = "/fonts/Montserrat/Montserrat-Medium.ttf".toByteBuffer()
        Aether.renderer.createFont("Montserrat-Regular", regular)
        ResourceProvider.registerFont("Montserrat-Regular", regular)
        Aether.renderer.createFont("Montserrat-Medium", medium)
        ResourceProvider.registerFont("Montserrat-Medium", medium)

        compose(name = "Test", modifier = CompositionModifier()
            .size(546.px, 336.px)
            .backgroundRadius(8.radius)
            .backgroundColor(RGBA(1f, 0f, 1f).rgb)
        ) {
            construct {
                val w = 384f
                val h = 288f
                it.modifier.constrain(50, 50, w, h)
                val handle = svg.retrieveImage(w, h)

                render {
                    color(-1)
                    renderImage(handle, it.x, it.y, it.width, it.height)
                }
            }


//            construct {
//                val handle = image.retrieveImage(34f, 37f)
//                it.modifier.constrain(50, 50,145, 37)
//                render {
//                    color(-1)
//                    renderImage(handle, it.x, it.y, 34f, 37f)
//                    color(RGBA(41, 45, 50))
//                    font("Montserrat-Medium", 24f, UITextAlignment.LEFT, UITextAlignment.TOP, 1.025f)
//                    "PRISM".render(it.x + 46, it.y + 1 + 5)
//
//                    color(RGBA(105, 116, 131))
//                    font("Montserrat-Regular", 8f, UITextAlignment.RIGHT, UITextAlignment.TOP, 0f)
//                    "v1.0.0-Beta".render(fontBounds()[2], it.y + 27f + 5f)
//                }
//            }
        }
    }
}