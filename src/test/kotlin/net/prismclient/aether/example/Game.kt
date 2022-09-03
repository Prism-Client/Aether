package net.prismclient.aether.example

import net.prismclient.aether.core.Aether
import net.prismclient.aether.example.util.Window
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.Platform
import kotlin.math.max

/**
 * An example game using LWJGL 3 and GLFW to run Aether.
 */
abstract class Game(val windowTitle: String) {
    lateinit var aether: Aether
    lateinit var window: Window

    var mouseX: Float = 0f
    var mouseY: Float = 0f

    open fun initialize() {
        createWindow()

        // Create the callbacks to handle Aether input and other things
        createCallbacks()
    }

    open fun createAether() {
        // Aether is an open (inheritable) class. It can be instantiated on its own,
        // however if a specific feature is needed, it can be extended to implement it.
        // Renderer is an object class provided within the example package which implements
        // UIRenderer and uses NanoVG to handle the functions.
        aether = Aether(Renderer)
    }

    open fun createWindow() {
        GLFWErrorCallback.createPrint().set()

//        Configuration.GLFW_CHECK_THREAD0.set(false)

        if (!glfwInit()) throw RuntimeException("Failed to init GLFW")
        if (Platform.get() === Platform.MACOSX) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        }

        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE)

        window = Window(glfwCreateWindow(1000, 600, windowTitle, MemoryUtil.NULL, MemoryUtil.NULL))

        if (window.handle == MemoryUtil.NULL) {
            glfwTerminate()
            throw RuntimeException("Failed to create the window.")
        }


        // Add some callbacks for window resizing and content scale
        glfwSetFramebufferSizeCallback(window.handle) { _, width, height ->
            window.width = width
            window.height = height

            // Update Aether as the window is resized
            aether.update(
                width / window.contentScaleX,
                height / window.contentScaleY,
                max(window.contentScaleX, window.contentScaleY)
            )
        }

        glfwSetWindowContentScaleCallback(window.handle) { _, x, y ->
            window.contentScaleX = x
            window.contentScaleY = y
        }

        glfwMakeContextCurrent(window.handle)
        createCapabilities()
        glfwSetTime(0.0)
        glfwSwapInterval(0)

        // Create Aether after we've initialized OpenGL.
        createAether()

        // Update the state of the window
        MemoryStack.stackPush().use {
            val width = it.mallocInt(1)
            val height = it.mallocInt(1)
            val contentScaleX = it.mallocFloat(1)
            val contentScaleY = it.mallocFloat(1)
            glfwGetFramebufferSize(window.handle, width, height)
            glfwGetWindowContentScale(window.handle, contentScaleX, contentScaleY)

            window.width = width[0]
            window.height = height[0]
            window.contentScaleX = contentScaleX[0]
            window.contentScaleY = contentScaleY[0]

            // Update Aether and pass the properties of the window. For
            // content scale, return the larger value of the two axes.
            aether.update(
                window.width / window.contentScaleX,
                window.height / window.contentScaleY,
                max(window.contentScaleX, window.contentScaleY)
            )
        }
    }

    /**
     * Creates the callbacks for input events. The window resize callback is set within [createWindow].
     */
    abstract fun createCallbacks()

    open fun run() {
        while (!glfwWindowShouldClose(window.handle)) {
            glViewport(0, 0, window.width, window.height)
            glClearColor(0.3f, 0.3f, 0.3f, 0f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

            // Render Aether :)
            aether.render()

            glfwSwapBuffers(window.handle)
            glfwPollEvents()
        }

        GL.setCapabilities(null)
        Callbacks.glfwFreeCallbacks(window.handle)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }
}