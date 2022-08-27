package net.prismclient.aether.example

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.input.MouseButtonType
import net.prismclient.aether.ui.screen.UIScreen
import org.lwjgl.glfw.GLFW.*

/**
 * Creates a new game with the provided [screen].
 *
 * @author sen
 * @since 1.0
 */
class Runner(private val screen: UIScreen) : Game("Runner") {
    init {
        initialize()
        Aether.displayScreen(screen)
        run()
    }

    override fun createCallbacks() {
        // Mouse move input
        glfwSetCursorPosCallback(window.handle) { _, x, y ->
            mouseX = x.toFloat()
            mouseY = y.toFloat()
            // Set the MouseButtonType to None to inform Aether that it is
            // a mouse move event instead of a mouse click or release event
            aether.mouseChanged(mouseX, mouseY, MouseButtonType.None, false)
        }

        // Mouse press / release input
        glfwSetMouseButtonCallback(window.handle) { _, button, action, _ ->
            val mouseButton = when (button) {
                GLFW_MOUSE_BUTTON_LEFT -> MouseButtonType.Primary
                GLFW_MOUSE_BUTTON_RIGHT -> MouseButtonType.Secondary
                GLFW_MOUSE_BUTTON_MIDDLE -> MouseButtonType.Middle
                else -> return@glfwSetMouseButtonCallback
            }

            // Inform Aether that the mouse has changed with the provided
            // mouse position, mouse button, and if the mouse is released.
            aether.mouseChanged(mouseX, mouseY, mouseButton, action == GLFW_RELEASE)
        }

        // Mouse scroll input
        glfwSetScrollCallback(window.handle) { _, xDst, yDst ->
            aether.mouseScrolled(xDst.toFloat(), yDst.toFloat())
        }

        // Text / keyboard input
        glfwSetKeyCallback(window.handle) { _, keyCode, scanCode, action, _ ->
            // Reset the screen when esc is pressed.
            if (glfwGetKeyName(keyCode, scanCode) == null) {
                if (action == GLFW_PRESS && keyCode == GLFW_KEY_ESCAPE) {
                    Aether.displayScreen(screen)
                }
            }

            // TODO: Aether keyboard input
        }
    }
}