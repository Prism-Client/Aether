package net.prismclient.aether.example.screens

import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.composable.Construct
import net.prismclient.aether.ui.composable.Label
import net.prismclient.aether.ui.composable.VerticalList
import net.prismclient.aether.ui.composition.DefaultCompositionModifier
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.composition.util.UIBorder
import net.prismclient.aether.ui.dsl.ComposeDSL.Composition
import net.prismclient.aether.ui.dsl.ComposeDSL.composable
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.dsl.Resource
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.ui.renderer.UIStrokeDirection
import net.prismclient.aether.ui.screen.UIScreen

/**
 * An example screen which shows some features of which Aether supports. Run the main method.
 *
 * @author sen
 * @since 1.0
 */
class MacrosExample : UIScreen {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Runner(MacrosExample())
        }
    }

    override fun compose() {
        val BACKGROUND_COLOR = (-1).rgb

        UIRegistry.registerStyle("FontStyle", FontStyle().fontType(FontType.AutoWidth))


        // Load the Montserrat Font
        Resource {
            fontCollection(localResource("/fonts/Montserrat/"))
        }

        Composition(
            name = "Test", modifier = DefaultCompositionModifier()
            .size(546.px, 336.px)
            .backgroundRadius(8.radius)
            .backgroundColor(BACKGROUND_COLOR)
        ) {
            val border = UIBorder()
            border.borderWidth = 1.px
            border.borderColor = RGBA(223, 224, 235).rgb
            border.borderDirection = UIStrokeDirection.INSIDE
            modifier.background!!.backgroundBorder = border

            // Title
            Label(
                text = "Macros",
                modifier = Modifier()
                    .position(32.px, 32.px),
                fontStyle = FontStyle()
                    .fontName("Montserrat-Bold")
                    .fontColor(0x252733.rgb)
                    .fontSize(19.px)
            )
            // Description below title
            Label(
                text = "Speed up your gameplay by utilizing macros!",
                modifier = Modifier()
                    .position(32, 64),
                fontStyle = FontStyle()
                    .fontName("Montserrat-Regular")
                    .fontColor(0x9FA2B4.rgb)
                    .fontSize(12.px)
            )
            // View all hyperlink label
            Label(
                text = "View All",
                modifier = Modifier().anchor(Alignment.TOPRIGHT)
                    .position(1.rel - 32.px, 32.px),
                fontStyle = FontStyle()
                    .fontName("Montserrat-SemiBold")
                    .fontColor(0x3751FF.rgb)
                    .fontSize(14.px)
            )

            VerticalList(
                modifier = LayoutModifier()
                    //.backgroundColor(RGBA(1f, 0f, 0f, 0.3f).rgb)
                    .constrain(0.px, 96.px, 1.rel, 232.px), // 328 - 96
                childSpacing = (-1).px
            ) {
                composable(DefaultMacro("Lorem Ipsum", false, Modifier())) {}
                composable(DefaultMacro("Lorem Ipsum", true, Modifier())) {}
            }
        }
    }

    fun lineBreak() {
        Construct(modifier = Modifier().size(1.rel, 1.px)) {
            Render {
                color(RGBA(223, 224, 235))
                rect(it.x, it.y, it.width, it.height)
            }
        }
    }

    class DefaultMacro(label: String, enabled: Boolean, modifier: UIModifier<*>) :
        Macro<DefaultMacro>(label, enabled, modifier) {
        override fun renderComponent() {
            super.renderComponent()
            renderLabel()
        }

        override fun copy(): DefaultMacro = DefaultMacro(label, enabled, modifier.copy())
    }

    abstract class Macro<T : Macro<T>>(val label: String, var enabled: Boolean = false, modifier: UIModifier<*>) :
        UIComponent<T>(modifier) {
        init {
            modifier.size(1.rel, 58.px)
            val background = UIBackground()
            val border = UIBorder()

            border.borderWidth = 1.px
            border.borderColor = RGBA(223, 224, 235).rgb
            border.borderDirection = UIStrokeDirection.INSIDE

            background.backgroundBorder = border
            modifier.background = background
        }

        override fun renderComponent() {
            Renderer {
                // Render the radio button or whatever to show that it is enabled/disabled
                if (enabled) {
                    // Render an ellipse and draw a checkmark over it
                    color(RGBA(55, 81, 255))
                    ellipse(x + 10f + 32f, y + (height / 2f), 10f, 10f)
                    color(RGBA(1f, 1f, 1f))
                    renderImage("solid/check", x + 32f + 5f, y + (height / 2f) - 3.5f, 10f, 7f)
                } else {
                    path {
                        hole {
                            // Create the bigger ellipse and a smaller ellipse
                            // within so that there is a 2px border. Offset the
                            // x by 32 px and center the y-axis.
                            ellipse(x + 10f + 32f, y + (height / 2f), 10f, 10f)
                            ellipse(x + 10f + 32f, y + (height / 2f), 8f, 8f)
                        }
                    }.fillPath(RGBA(197, 199, 205))
                }
            }
        }

        /**
         * Renders a label and returns the x ending position of it
         */
        open fun renderLabel(): Float {
            Renderer {
                color(RGBA(37, 39, 51))
                font("Montserrat-Medium", 14f, UITextAlignment.LEFT, UITextAlignment.CENTER, 0f)
                label.render(x + 68, y + height / 2f)
            }
            return fontBounds()[0] + fontBounds()[2]
        }
    }
}