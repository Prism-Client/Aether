import net.prismclient.aether.core.event.UIEventBus
import net.prismclient.aether.core.event.type.MouseMoveEvent
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.button
import net.prismclient.aether.ui.component.compose
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.ui.registry.register
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.shorthands.*

class TestScreen : UIScreen {
    override fun createScreen() {
        // Composition Modifier
        register(
            CompositionModifier()
                .backgroundColor(0x202020.rgb)
                .backgroundRadius(25.radius)
                .anchor(UIAlignment.CENTER)
        )

        // Font style
        register(
            FontStyle()
                .fontName("Poppins")
                .position(0.5.crel, 0.5.crel)
                .size(0, 0)
                .fontAnchor(UIAlignment.CENTER)
                .textResizing(TextResizing.AutoWidth)
                .fontAlignment(UITextAlignment.CENTER, UITextAlignment.CENTER)
                .offsetBaseline()
        )

        compose("Test") {
            modifier.constrain(0.5.rel, 0.5.rel, 500.px, 500.px)
            button(
                text = "AgdCiLMmzjt",
                modifier = Modifier()
                    .control(UIAlignment.TOPCENTER)
                    .size(0.9.rel, 50.px)
                    .backgroundColor(0x29CC97.rgb)
                    .backgroundRadius(9.radius),
                fontStyle = FontStyle()
                    .fontSize(16.px)
                    .fontColor(RGBA(255, 255, 255).rgb)
            ).apply {
//                mouseMoved {
//                    println("This mouse moved! (${it.mouseX}, ${it.mouseY})")
//                }
            }
        }

        UIEventBus.register<MouseMoveEvent> {
            println("Hello")
        }
    }
}