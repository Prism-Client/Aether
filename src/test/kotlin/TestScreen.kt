import net.prismclient.aether.ui.component.type.button
import net.prismclient.aether.ui.composition.constrain
import net.prismclient.aether.ui.modifier.*
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.shorthands.*

class TestScreen : UIScreen {
    override fun createScreen() {


        val pixelUnit = 0xFFFFFF

        // Creates a composition with in the screen
        compose("Test") {
            val btn = button(
                text = "Hello",
                modifier = Modifier()
                    .constrain(50, 50, 100, 100)
                    .padding(10.px, 5.px, 10.px, 5.px)
                    .backgroundColor(0xFFFFFF.rgb) // RGBA(255, 255, 255, 255).rgba //
            )
            components.add(btn)
            btn.parent = this
        }.constrain(50, 50, 300, 300)
    }
}