import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.component.type.button
import net.prismclient.aether.ui.composition.constrain
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.modifier.*
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.util.shorthands.*
import kotlin.random.Random

class TestScreen : UIScreen {
    override fun createScreen() {
        compose(
            name = "Test",
            modifier = Modifier()
                .backgroundColor(0x202020.rgb)
                .backgroundRadius(25.radius)
        ) {
//            for (i in 0 .. 1000) {
                val btn = button(
                    text = "AgdCiLMmzjt",
                    modifier = Modifier()
                        .position(25, 25)
                        .size(150.px, 30.px)
                        .backgroundColor(0x29CC97.rgb)
                        .backgroundRadius(9.radius),
                    fontStyle = FontStyle()
                        .fontName("Poppins")
                        .fontColor(RGBA(255, 255, 255).rgb)
                        .fontSize(16.px)
                        .position(0.5.crel, 0.5.crel)
                        .size(1.crel, 1.crel)
                        .fontAnchor(UIAlignment.CENTER)

                )
                components.add(btn)
                btn.parent = this
//            }
        }.constrain(50, 50, 500, 500)
    }
}