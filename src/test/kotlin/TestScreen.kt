import net.prismclient.aether.ui.component.type.button
import net.prismclient.aether.ui.composition.constrain
import net.prismclient.aether.ui.modifier.*
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.shorthands.compose
import net.prismclient.aether.ui.util.shorthands.px
import net.prismclient.aether.ui.util.shorthands.rel

class TestScreen : UIScreen {
    override fun createScreen() {
        val pixelUnit = 50.px // Java: px(50)
        val relativeUnit = 0.5.rel

        // Creates a composition with in the screen
        compose("Test") {
            val btn = button(
                text = "Hello",
                modifier = Modifier()
                    .constrain(50, 50, 100, 100)
                    .padding(10.px, 5.px, 10.px, 5.px)
                    //.backgroundColor(0xFF0000)
            )
            components.add(btn)
        }.constrain(50, 50, 300, 300)
    }
}