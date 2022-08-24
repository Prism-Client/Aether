package net.prismclient.aether.example.screens

import com.sun.javafx.iio.ios.IosImageLoader.RGB
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.compose
import net.prismclient.aether.ui.component.label
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.registry.register
import net.prismclient.aether.ui.resource.UIResourceProvider
import net.prismclient.aether.ui.screen.UIScreen

/**
 * An example screen which shows some features of which Aether supports. Run the main method.
 *
 * @author sen
 * @since 1.0
 */
class TestScreen : UIScreen {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Runner(TestScreen())
        }
    }

    override fun createScreen() {
        val BACKGROUND_COLOR = (-1).rgb
        val FONT = FontStyle() // Define the base font style
                .fontSpacing(0.1.px)
                .fontType(FontType.AutoWidth)
                .fontAlignment(UITextAlignment.LEFT, UITextAlignment.TOP)
                .offsetBaseline()

        UIAssetDSL.bulkLoad("/fonts/Montserrat/")

        // TODO: Warning when failed to reference font

        register(
                name = "Bold",
                style = FONT.fontName("Montserrat/Montserrat-Bold")
        )

        register(
                name = "SemiBold",
                style = FONT.fontName("Montserrat/Montserrat-SemiBold")
        )

        register(
                name = "Medium",
                style = FONT.fontName("Montserrat/Montserrat-Medium")
        )

        register(
                name = "Regular",
                style = FONT.fontName("Montserrat/Montserrat-Regular")
        )

        compose(
                name = "Test",
                modifier = CompositionModifier()
                        .size(546.px, 336.px)
                        .backgroundRadius(8.radius)
                        .backgroundColor(BACKGROUND_COLOR)
        ) {
            // Title
            label(
                    text = "Macros",
                    modifier = Modifier()
                            .position(32.px, 32.px),
                    fontStyle = FontStyle()
                            .of("Bold")
                            .fontColor(0x252733.rgb)
                            .fontSize(19.px)
            )
            // Description below title
            label(
                    text = "Speed up your gameplay by utilizing macros to complete actions!",
                    modifier = Modifier()
                            .position(32, 64),
                    fontStyle = FontStyle().of("Regular")
                            .fontColor(0x9FA2B4.rgb)
                            .fontSize(12.px)
            )
            // View all hyperlink label
            label(
                    text = "View All",
                    modifier = Modifier()
                            .anchor(UIAlignment.TOPRIGHT)
                            /* TODO: Full Font and Background shorthands */
                            //.backgroundColor(RGBA(1f, 0f, 0f, 0.3f).rgba)
                            /* TODO: Align function for Modifiers*/
                            .position(1.rel - 32.px, 32.px),
                    fontStyle = FontStyle().of("SemiBold")
                            .fontColor(0x3751FF.rgb)
                            .fontSize(14.px)
            )
        }
    }

    fun lineBreak() {
        // construct {
        //     render {
        //          rectangle()
        //     }
        // }
    }
}