package net.prismclient.aether.example.screens

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.component
import net.prismclient.aether.ui.component.compose
import net.prismclient.aether.ui.component.construct
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.image.ImageProvider
import net.prismclient.aether.ui.layout.AutoLayout
import net.prismclient.aether.ui.layout.LayoutModifier
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

    }
}