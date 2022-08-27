package net.prismclient.aether.example.screens

import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.util.shorthands.RGBA
import net.prismclient.aether.core.util.shorthands.px
import net.prismclient.aether.core.util.shorthands.radius
import net.prismclient.aether.core.util.shorthands.rgb
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.component.compose
import net.prismclient.aether.ui.component.construct
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.image.GENERATE_MIPMAPS
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.image.REPEATX
import net.prismclient.aether.ui.image.REPEATY
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

        compose(name = "Test", modifier = CompositionModifier()
            .size(546.px, 336.px)
            .backgroundRadius(8.radius)
            .backgroundColor(RGBA(1f, 1f, 1f).rgb)
        ) {
            construct {
                it.modifier.constrain(50, 50,128, 140)
                render {
                    val handle = image.retrieveImage(it.width, it.height)
                    color(-1)
                    renderImage(handle, it.x, it.y, it.width, it.height)
                }
            }

            construct {
                it.modifier.constrain(50 + 128, 50,64, 70)
                render {
                    val handle = image.retrieveImage(it.width, it.height)
                    color(-1)
                    renderImage(handle, it.x, it.y, it.width, it.height)
                }
            }

            construct {
                it.modifier.constrain(50 + 128 + 64, 50,32, 35)
                render {
                    val handle = image.retrieveImage(it.width, it.height)
                    color(-1)
                    renderImage(handle, it.x, it.y, it.width, it.height)
                }
            }
        }
    }
}