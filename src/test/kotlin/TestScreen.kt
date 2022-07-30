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
                .backgroundColor(0xFFFFFF.rgb)
//                .backgroundRadius(9.radius)
        ) {
//            for (i in 0 .. 1000) {
                val btn = button(
                    text = "Hello",
                    modifier = Modifier()
                        .position(5, 5)
                        .size(250.px, 250.px)
                        .backgroundColor(0x57A4FF.rgb),
//                        .backgroundRadius(5.radius),
                    fontStyle = FontStyle()
                        .fontName("Poppins")
                        .fontColor(0xFF0000.rgb)
                        .fontSize(32.px)
                        .position(40, 40)
                        .fontAnchor(AnchorPoint(0.5.crel, 0.5.crel))

                )
                components.add(btn)
                btn.parent = this
//            }
        }.constrain(50, 50, 500, 500)
    }
}