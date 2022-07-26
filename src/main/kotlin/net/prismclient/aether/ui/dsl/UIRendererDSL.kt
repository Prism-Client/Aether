package net.prismclient.aether.ui.dsl

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.Alignment.*
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.renderer.UIFramebuffer
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.UIStrokeDirection
import net.prismclient.aether.ui.unit.other.UIRadius

/**
 * Executes the given [block] within the [UIRendererDSL] scope.
 */
inline fun Renderer(block: Block<UIRendererDSL>) = UIRendererDSL.block()

/**
 * [UIRendererDSL] wraps the [UIRenderer] to provide utility to functions, and to minimize the
 * of code necessary to draw general shapes on screen with Kotlin. It also supplies support
 * functions for more advanced rendering features that the UIRenderer does not directly support.
 *
 * @author sen
 * @since 1.0
 */
object UIRendererDSL {
    @JvmStatic
    val renderer: UIRenderer get() = Aether.renderer

    @JvmStatic
    var activeColor: Int = 0
        private set

    /**
     * Informs the [UIRendererDSL] that the active calls are a stroke. This is set with [stroke].
     *
     * @see stroke
     */
    @JvmStatic
    var isStroke: Boolean = false

    /**
     * The active stroke width set with [stroke]. This does not reflect the renderer's stroke width
     * directly.
     */
    @JvmStatic
    var activeStrokeWidth: Float = 0f

    /**
     * The color of the active stroke set with [stroke].
     */
    @JvmStatic
    var activeStrokeColor: Int = 0

    /**
     * The active stroke direction set with [stroke].
     *
     * @see stroke
     */
    @JvmStatic
    var activeStrokeDirection: UIStrokeDirection = UIStrokeDirection.CENTER

    /**
     * Informs the renderer to prepare to render a frame of the given size.
     *
     * @param devicePxRatio The device pixel ratio of the device.
     */
    @JvmStatic
    fun beginFrame(displayWidth: Float, displayHeight: Float, devicePxRatio: Float) =
        renderer.beginFrame(displayWidth, displayHeight, devicePxRatio)

    /**
     * Informs the renderer to render all the calls, and flush the paths.
     */
    @JvmStatic
    fun endFrame() = renderer.endFrame()

    /**
     * Sets the fill color to the given color.
     */
    @JvmStatic
    fun color(color: Int) {
        activeColor = color
        renderer.color(color)
    }

    /**
     * Sets the fill color to the given color.
     */
    @JvmStatic
    fun color(color: UIColor?) = color(color?.rgba ?: 0)

    // -- Font -- //

    /**
     * Applies the given font values to the active context.
     */
    @JvmStatic
    fun font(
        fontFace: String,
        fontSize: Float,
        horizontalAlignment: UITextAlignment,
        verticalAlignment: UITextAlignment,
        fontSpacing: Float
    ) {
        renderer.fontFace(fontFace)
        renderer.fontSize(fontSize)
        renderer.fontAlignment(horizontalAlignment, verticalAlignment)
        renderer.fontSpacing(fontSpacing)
    }

    /**
     * Renders the given string at the given position
     */
    @JvmStatic
    fun String.render(x: Float, y: Float) = renderer.renderText(this@render, x, y)

    /**
     * Aligns the given string based on the [alignment] within the [width] and [height] boundaries.
     */
    @JvmStatic
    fun String.render(alignment: Alignment, x: Float, y: Float, width: Float, height: Float) {
        val alignedX: Float = when (alignment) {
            TOPCENTER, CENTER, BOTTOMCENTER -> width / 2f
            TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> width
            else -> 0f
        } + x
        val alignedY: Float = when (alignment) {
            MIDDLELEFT, CENTER, MIDDLERIGHT -> height / 2f
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> height
            else -> 0f
        } + y

        this.render(alignedX, alignedY)
    }

    // -- General Rendering -- //

    /**
     * renders a rectangle with the given bounds and [radius].
     */
    @JvmStatic
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) = rect(
        x,
        y,
        width,
        height,
        radius?.topLeft.dp,
        radius?.topRight.dp,
        radius?.bottomRight.dp,
        radius?.bottomLeft.dp
    )

    /**
     * Renders a rectangle with a single radius value.
     */
    @JvmStatic
    @JvmOverloads
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        rect(x, y, width, height, radius, radius, radius, radius)

    /**
     * Renders a rectangle with multiple radius values.
     */
    @JvmStatic
    fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) {
        path {
            rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        }.fillPath()
        if (isStroke) {
            val stroke = activeStrokeWidth
            path {
                hole {
                    when (activeStrokeDirection) {
                        UIStrokeDirection.CENTER -> {
                            rect(
                                x - stroke / 2f,
                                y - stroke / 2f,
                                width + stroke,
                                height + stroke,
                                topLeft,
                                topRight,
                                bottomRight,
                                bottomLeft
                            )
                            rect(
                                x + stroke / 2f,
                                y + stroke / 2f,
                                width - stroke,
                                height - stroke,
                                topLeft,
                                topRight,
                                bottomRight,
                                bottomLeft
                            )
                        }

                        UIStrokeDirection.INSIDE -> {
                            rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
                            rect(
                                x + stroke,
                                y + stroke,
                                width - stroke - stroke,
                                height - stroke - stroke,
                                topLeft,
                                topRight,
                                bottomRight,
                                bottomLeft
                            )
                        }

                        UIStrokeDirection.OUTSIDE -> {
                            rect(
                                x - stroke,
                                y - stroke,
                                width + (stroke * 2),
                                height + (stroke * 2),
                                topLeft,
                                topRight,
                                bottomRight,
                                bottomLeft
                            )
                            rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
                        }
                    }
                }
            }.fillPath(activeStrokeColor)
        }
    }

    /**
     * Renders an ellipse with the given [x]. [y], [width], and [height].
     */
    @JvmStatic
    fun ellipse(x: Float, y: Float, width: Float, height: Float) {
        path {
            ellipse(x, y, width, height)
        }.fillPath()
    }

    /**
     * Renders an image with the given [imageName] at the given coordinates with no corner rounding.
     */
    @JvmStatic
    fun renderImage(imageName: String, x: Float, y: Float, width: Float, height: Float) =
        renderImage(imageName, x, y, width, height, 0f, 0f, 0f, 0f)

    /**
     * Renders an image of [imageName] at the given positions with varying rounded corners. If the [imageName] is
     * not found, the image will not be rendered, and instead a rectangle with the color of the image is rendered.
     */
    @JvmStatic
    fun renderImage(
        imageName: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) {
        path {
            imagePattern(
                imageName,
                x,
                y,
                width,
                height,
                0f,
                1f
            )
            rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        }.fillPaint()
    }

    // -- Util -- //

    /**
     * Converts the given float from degrees to radians.
     */
    @JvmStatic
    fun Float.toRad() = renderer.degToRad(this)

    /**
     * Converts the given float from radians to degrees.
     */
    @JvmStatic
    fun Float.toDeg() = renderer.radToDeg(this)

    /**
     * Returns the x offset of the given string base on the index
     */
    @JvmStatic
    fun String.indexOffset(index: Int): Float {
        var w = 0f
        if (index > this.length - 1) return fontBounds().advance + fontBounds().minX
        for (i in 0 until index) w += this[i].toString().bounds.advance
        return w + fontBounds().minX
    }

    /**
     * Creates a new path and invokes the code within the [block]. To automatically close the path
     * set [closePath] to true. Closing the path makes a line to the initial starting point.
     *
     * @param closePath Closes the path if true.
     */
    @JvmStatic
    inline fun path(closePath: Boolean = false, block: Block<UIPathDSL>): UIPathDSL {
        UIPathDSL.beginPath()
        UIPathDSL.block()
        if (closePath) UIPathDSL.closePath()
        return UIPathDSL
    }

    /**
     * A complimentary value for Compositions. When false, the save block will do nothing.
     */
    var shouldSave: Boolean = true

    /**
     * Automatically saves and restores the state within this block. Any translations
     * and other states such as scissor are saved within the state. [shouldSave] must be true.
     */
    @JvmStatic
    inline fun save(block: Block<UIRendererDSL>): UIRendererDSL {
        if (shouldSave) renderer.save()
        UIRendererDSL.block()
        if (shouldSave) renderer.restore()
        return UIRendererDSL
    }

    /**
     * Saves the state (and automatically restores it) and translates the [block] by the given [x] and [y] values.
     */
    @JvmStatic
    inline fun translate(x: Float, y: Float, block: Block<UIRendererDSL>): UIRendererDSL = save {
        renderer.translate(x, y)
        block()
    }


    /**
     * Scissors (clips) any content that exceeds the give bounds.
     */
    @JvmStatic
    inline fun scissor(
        x: Float, y: Float, width: Float, height: Float, block: Block<UIRendererDSL>
    ): UIRendererDSL = save {
        renderer.scissor(x, y, width, height)
        UIRendererDSL.block()
    }

    /**
     * Informs [UIRendererDSL] that anything within this [block] is should be a stroke.
     *
     * @see StrokeDirection
     */
    @JvmStatic
    inline fun stroke(
        strokeWidth: Float, strokeColor: Int, strokeDirection: UIStrokeDirection, block: Block<UIRendererDSL>
    ): UIRendererDSL {
        activeStrokeWidth = strokeWidth
        activeStrokeColor = strokeColor
        activeStrokeDirection = strokeDirection
        isStroke = true
        UIRendererDSL.block()
        isStroke = false
        return UIRendererDSL
    }

    /**
     * Begins a new frame and binds the given [framebuffer]. The [block] will be executed and the frame
     * will be drawn and the framebuffer will be unbound.
     */
    @JvmStatic
    inline fun renderToFramebuffer(framebuffer: UIFramebuffer, block: Block<UIRendererDSL>): UIRendererDSL {
        renderer.bindFBO(framebuffer)
        beginFrame(framebuffer.width, framebuffer.height, framebuffer.contentScale)
        UIRendererDSL.block()
        endFrame()
        renderer.unbindFBO()
        return UIRendererDSL
    }

    /**
     * Depending on the renderer used, the graphics might not be drawn until after the frame is ended. If something is
     * intended to be drawn over the UI just created, running this will end the current frame, thus drawing the content,
     * runs the block, and begins a new frame after the block is completed. The new frame is begun based on the
     * properties of [Aether.displayWidth] and so forth.
     */
    @JvmStatic
    inline fun endAndBeginFrame(block: () -> Unit) {
        endFrame()
        block()
        beginFrame(Aether.instance.displayWidth, Aether.instance.displayHeight, Aether.instance.devicePixelRatio)
    }
}