import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.debug.UIDebug
import net.prismclient.aether.core.util.extensions.toByteBuffer
import net.prismclient.aether.core.input.MouseButtonType
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.core.input.UIKey
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.Platform
import kotlin.math.max


/**
 * An example runner class with LWJGL 3 & glfw
 */
object Runner {
    var mouseX = 0.0
        private set
    var mouseY = 0.0
        private set
    var framebufferWidth = 0
    var framebufferHeight = 0
    var contentScaleX = 0f
    var contentScaleY = 0f
    var core: Aether? = null

    val keymap = HashMap<Int, UIKey>()

    init {
        // Map the GLFW key mappings to UIKey
        org.lwjgl.glfw.GLFW::class.java.declaredFields.forEach { field ->
            if (field.name.startsWith("GLFW_KEY_")) {
                val fieldName = field.name.removePrefix("GLFW_")
                UIKey::class.java.enumConstants.forEach {
                    if (it.name.contentEquals(fieldName)) {
                        keymap[field.get(null) as Int] = it
                    }
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        GLFWErrorCallback.createPrint().set()

        if (!glfwInit()) throw RuntimeException("Failed to init GLFW")
        if (Platform.get() === Platform.MACOSX) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        }

        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE)

        val window = glfwCreateWindow(1000, 600, "Demo", MemoryUtil.NULL, MemoryUtil.NULL)

        if (window == MemoryUtil.NULL) {
            glfwTerminate()
            throw RuntimeException()
        }

        glfwSetMouseButtonCallback(window) { _: Long, button: Int, action: Int, _: Int ->
            //core!!.mouseChanged(button, action == GLFW_RELEASE)
            core!!.mouseChanged(
                mouseX.toFloat(), mouseY.toFloat(), when (button) {
                    GLFW_MOUSE_BUTTON_1 -> MouseButtonType.Primary
                    GLFW_MOUSE_BUTTON_2 -> MouseButtonType.Secondary
                    GLFW_MOUSE_BUTTON_3 -> MouseButtonType.Middle
                    else -> throw RuntimeException("Weird button pressed: $button")
                }, action == GLFW_RELEASE
            )
        }
        glfwSetCursorPosCallback(window) { _: Long, xpos: Double, ypos: Double ->
            mouseX = xpos
            mouseY = ypos
            core!!.mouseChanged(mouseX.toFloat(), mouseY.toFloat(), MouseButtonType.None, false)
        }

        glfwSetWindowContentScaleCallback(window) { _: Long, xscale: Float, yscale: Float ->
            contentScaleX = xscale
            contentScaleY = yscale
        }

        glfwSetFramebufferSizeCallback(window) { _: Long, width: Int, height: Int ->
            if (width > 0 && height > 0) {
                framebufferWidth = width
                framebufferHeight = height
                core!!.update(width / contentScaleX, height / contentScaleY, max(contentScaleX, contentScaleY))
            }
        }

        glfwSetKeyCallback(window) { _: Long, keyCode: Int, scanCode: Int, action: Int, _: Int ->
            // Check if the key is null
            if (glfwGetKeyName(keyCode, scanCode) == null) {
                if (action == GLFW_PRESS && keyCode == GLFW_KEY_ESCAPE) {
                    createScreen(args)
                }
            }
//                val isRelease = action == GLFW_RELEASE
//                when (keyCode) {
//                    GLFW_KEY_LEFT_CONTROL -> Aether.updateModifierKey(UIModifierKey.LEFT_CTRL, isRelease)
//                    GLFW_KEY_RIGHT_CONTROL -> Aether.updateModifierKey(UIModifierKey.RIGHT_CTRL, isRelease)
//                    GLFW_KEY_LEFT_SHIFT -> Aether.updateModifierKey(UIModifierKey.LEFT_SHIFT, isRelease)
//                    GLFW_KEY_RIGHT_SHIFT -> Aether.updateModifierKey(UIModifierKey.RIGHT_SHIFT, isRelease)
//                    GLFW_KEY_LEFT_ALT -> Aether.updateModifierKey(UIModifierKey.LEFT_ALT, isRelease)
//                    GLFW_KEY_RIGHT_ALT -> Aether.updateModifierKey(UIModifierKey.RIGHT_ALT, isRelease)
//                    GLFW_KEY_LEFT -> Aether.updateModifierKey(UIModifierKey.ARROW_LEFT, isRelease)
//                    GLFW_KEY_RIGHT -> Aether.updateModifierKey(UIModifierKey.ARROW_RIGHT, isRelease)
//                    GLFW_KEY_UP -> Aether.updateModifierKey(UIModifierKey.ARROW_UP, isRelease)
//                    GLFW_KEY_DOWN -> Aether.updateModifierKey(UIModifierKey.ARROW_DOWN, isRelease)
//                    GLFW_KEY_TAB -> Aether.updateModifierKey(UIModifierKey.TAB, isRelease)
//                    GLFW_KEY_ESCAPE -> Aether.updateModifierKey(UIModifierKey.ESCAPE, isRelease)
//                    GLFW_KEY_ENTER -> Aether.updateModifierKey(UIModifierKey.ENTER, isRelease)
//                    GLFW_KEY_CAPS_LOCK, GLFW_MOD_CAPS_LOCK -> Aether.updateModifierKey(
//                            UIModifierKey.CAPS_LOCK,
//                            isRelease
//                    )
//                    GLFW_KEY_BACKSPACE -> Aether.updateModifierKey(UIModifierKey.BACKSPACE, isRelease)
//                }
//            } else {
//                // glfwSetCharCallback is not invoked while ctrl is held
//                if (Aether.modifierKeys[UIModifierKey.LEFT_CTRL] != true) {
//                    when (glfwGetKeyName(keyCode, scanCode)!!.lowercase()[0]) {
//                        'a' -> Aether.instance.keyPressed('a')
//                        'c' -> Aether.instance.keyPressed('c')
//                        'v' -> Aether.instance.keyPressed('v')
//                        'x' -> Aether.instance.keyPressed('x')
//                        'z' -> Aether.instance.keyPressed('z')
//                        'y' -> Aether.instance.keyPressed('y')
//                    }
//                }
//            }
        }

        glfwSetCharCallback(window) { window, codepoint ->
//            Aether.instance.keyPressed(Character.toChars(codepoint)[0])
        }

        glfwSetScrollCallback(window) { _, xDst, yDst ->
            core!!.mouseScrolled(xDst.toFloat(), yDst.toFloat())
        }

//        glfwSetScrollCallback(window) { _: Long, _: Double, yscroll: Double -> core!!.mouseScrolled(yscroll.toFloat()) }

        glfwMakeContextCurrent(window)
        GL.createCapabilities()
        glfwSetTime(0.0)
        glfwSwapInterval(0)

        core = Aether(Renderer)

        MemoryStack.stackPush().use {
            val fw = it.mallocInt(1)
            val fh = it.mallocInt(1)
            val sx = it.mallocFloat(1)
            val sy = it.mallocFloat(1)
            glfwGetFramebufferSize(window, fw, fh)
            framebufferWidth = fw[0]
            framebufferHeight = fh[0]
            glfwGetWindowContentScale(window, sx, sy)
            contentScaleX = sx[0]
            contentScaleY = sy[0]

            core!!.update(
                framebufferWidth / contentScaleX, framebufferHeight / contentScaleY, max(contentScaleX, contentScaleY)
            )
        }

        var actualFps = 0
        var fps = 0
        var time = System.currentTimeMillis()
        var fpsstring = "FPS: $fps"

        val buffer = "/Poppins.ttf".toByteBuffer()
        Aether.renderer.createFont("Poppins", buffer)

        UIDebug.debug()
        createScreen(args)

        while (!glfwWindowShouldClose(window)) {
            //updateMouse(mouseX.toFloat(), mouseY.toFloat())

//            core!!.renderFrames()

            GL11.glViewport(0, 0, framebufferWidth, framebufferHeight)
            GL11.glClearColor(0.3f, 0.3f, 0.3f, 0f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)

            core!!.render()

            renderer {
                // 400 100, 250, 50
                beginFrame(
                    framebufferWidth / contentScaleX,
                    framebufferHeight / contentScaleY,
                    max(contentScaleX, contentScaleY)
                )
                color(-1)
                font("Poppins", 16f, UITextAlignment.CENTER, UITextAlignment.TOP, 0f)
                fpsstring.render((framebufferWidth / contentScaleX) / 2f, 0f)

                endFrame()
            }

            glfwSwapBuffers(window)
            glfwPollEvents()

            actualFps++
            if (System.currentTimeMillis() > time + 1000L) {
                fps = actualFps
                actualFps = 0
                time = System.currentTimeMillis()
                fpsstring = "FPS: $fps"
            }
        }
        GL.setCapabilities(null)
        Callbacks.glfwFreeCallbacks(window)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }

    fun createScreen(args: Array<String>) {
        Aether.displayScreen(TestScreen())
//        if (args.isNotEmpty()) {
//            Aether.displayScreen(
//                    when (args[0]) {
//                        "Animations" -> Animations()
//                        "PathRendering" -> PathRendering()
//                        else -> Default()
//                    }
//            )
//        } else Aether.displayScreen(PrismScreen())
    }
}