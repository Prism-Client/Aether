package net.prismclient.aether.example

import net.prismclient.aether.core.Aether
import net.prismclient.aether.example.screens.TestScreen
import net.prismclient.aether.ui.screen.UIScreen
import org.lwjgl.glfw.GLFW

/**
 * Creates a new game with the provided [screen].
 *
 * @author sen
 * @since 1.0
 */
class Runner(val screen: UIScreen) : Game("Runner") {
    init {
        initialize()

        Aether.displayScreen(screen)

        run()
    }

    override fun createCallbacks() {
        GLFW.glfwSetKeyCallback(window.handle) { _, keyCode, scanCode, action, _ ->
            // Reset the screen when esc is pressed.
            if (GLFW.glfwGetKeyName(keyCode, scanCode) == null) {
                if (action == GLFW.GLFW_PRESS && keyCode == GLFW.GLFW_KEY_ESCAPE) {
                    Aether.displayScreen(screen)
                }
            }
        }
    }
}