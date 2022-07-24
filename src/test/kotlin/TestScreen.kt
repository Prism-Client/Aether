import net.prismclient.aether.ui.composition.component.type.button
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.shorthands.compose

class TestScreen : UIScreen {
    override fun createScreen() {
        compose("Test") {
            constrain(50, 50, 300, 300)

            val button = button {
                constrain(0, 0, 100, 100)
            }
            components.add(button)
        }
    }
}