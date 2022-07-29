import net.prismclient.aether.ui.component.type.button
import net.prismclient.aether.ui.composition.constrain
import net.prismclient.aether.ui.modifier.*
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.shorthands.*
import kotlin.random.Random

class TestScreen : UIScreen {
    override fun createScreen() {
        compose(
            name = "Test",
            modifier = Modifier()
                .backgroundColor(0xFFFFFF.rgb)
                .backgroundRadius(9.radius)
        ) {
            for (i in 0 .. 1000) {
                val btn = button(
                    text = "Hello",
                    modifier = Modifier()
                        .position(Random.nextInt(300), Random.nextInt(300))//.position(5, 5)
                        .size(50.px, 50.px)
                        .backgroundColor(RGBA(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)).rgb)//.backgroundColor(0x57A4FF.rgb)
                        .backgroundRadius(5.radius)
                )
                components.add(btn)
                btn.parent = this
            }
        }.constrain(50, 50, 300, 300)
    }
}