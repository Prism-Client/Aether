package net.prismclient.aether.example

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.shorthands.alpha
import net.prismclient.aether.core.util.shorthands.blue
import net.prismclient.aether.core.util.shorthands.green
import net.prismclient.aether.core.util.shorthands.red
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.renderer.UIFramebuffer
import net.prismclient.aether.ui.renderer.UIRenderer
import org.lwjgl.nanovg.*
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.opengl.GL11
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImageResize
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

/**
 * Aether allows for the ability to provide your own rendering system. However, you might not
 * want to write a rendering system, or simply don't know how. The class provides an implementation
 * with NanoVG which is a fast vector graphics rendering library.
 *
 * @author sen
 * @since 1.0
 */
object Renderer : UIRenderer {
    private val framebuffers: HashMap<UIFramebuffer, NVGLUFramebuffer> = hashMapOf()
    private val images: HashMap<String, Int> = hashMapOf()

    private val ctx: Long = nvgCreate(NVG_ANTIALIAS)
    private val fillColor: NVGColor = NVGColor.create()
    private val strokeColor: NVGColor = NVGColor.create()
    private val gradient1: NVGColor = NVGColor.create()
    private val gradient2: NVGColor = NVGColor.create()
    private var paint: NVGPaint = NVGPaint.create() // TODO: Remove allocating paints

    private var activeColor: Int = 0

    private var fontBounds: FloatArray = FloatArray(5)
    private var ascender: FloatArray = FloatArray(1)
    private var descender: FloatArray = FloatArray(1)

    override fun beginFrame(width: Float, height: Float, devicePxRatio: Float) =
        nvgBeginFrame(ctx, width, height, devicePxRatio)

    override fun endFrame() = nvgEndFrame(ctx)

    override fun cancelFrame() = nvgCancelFrame(ctx)

    override fun save() = nvgSave(ctx)

    override fun restore() = nvgRestore(ctx)

    override fun reset() = nvgReset(ctx)

    override fun color(color: Int) {
        activeColor = color
        nvgColor(color, fillColor)
        nvgFillColor(ctx, fillColor)
    }

    override fun globalAlpha(alpha: Float) = nvgGlobalAlpha(ctx, alpha)

    override fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float) =
        nvgTransform(ctx, a, b, c, d, e, f)

    override fun translate(x: Float, y: Float) = nvgTranslate(ctx, x, y)

    override fun scale(x: Float, y: Float) = nvgScale(ctx, x, y)

    override fun rotate(angle: Float) = nvgRotate(ctx, angle)

    override fun skewX(angle: Float) = nvgSkewX(ctx, angle)

    override fun skewY(angle: Float) = nvgSkewY(ctx, angle)

    override fun scissor(x: Float, y: Float, w: Float, h: Float) = nvgScissor(ctx, x, y, w, h)

    override fun resetScissor() = nvgResetScissor(ctx)

    override fun resetTransformation() = nvgResetTransform(ctx)

    override fun useAntialiasing(antialiasing: Boolean) = nvgShapeAntiAlias(ctx, antialiasing)

    override fun createFBO(width: Float, height: Float): UIFramebuffer {
        val contentScale = Aether.instance.devicePixelRatio
        val framebuffer = nvgluCreateFramebuffer(
            ctx,
            (width * contentScale).toInt().coerceAtLeast(1),
            (height * contentScale).toInt().coerceAtLeast(1),
            NVG_IMAGE_REPEATX or NVG_IMAGE_REPEATY
        ) ?: throw RuntimeException("Failed to create the framebuffer. w: $width, h: $height")
        val fbo = UIFramebuffer(
            framebuffer.image(), width, height, width * contentScale, height * contentScale, contentScale
        )
        framebuffers[fbo] = framebuffer

        return fbo
    }

    override fun deleteFBO(fbo: UIFramebuffer) {
        val framebuffer = framebuffers[fbo]
        if (framebuffer != null) {
            nvgluDeleteFramebuffer(ctx, framebuffer)
            framebuffers.remove(fbo)
            return
        }
        println("Failed to delete the framebuffer because it was not found.")
    }

    override fun bindFBO(fbo: UIFramebuffer) {
        nvgluBindFramebuffer(
            ctx, framebuffers[fbo] ?: throw NullPointerException("Unable to find the framebuffer $fbo.")
        )
        GL11.glViewport(0, 0, fbo.scaledWidth.toInt(), fbo.scaledHeight.toInt())
        GL11.glClearColor(0f, 0f, 0f, 0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)
    }

    override fun unbindFBO() {
        nvgluBindFramebuffer(ctx, null)
    }

    override fun createImage(imageBuffer: ByteBuffer): UIRenderer.GeneratedImage {
        val width = IntArray(1)
        val height = IntArray(1)
        val image: ByteBuffer = STBImage.stbi_load_from_memory(imageBuffer, width, height, intArrayOf(0), 4)
            ?: throw RuntimeException("Failed to load the image.")
        return UIRenderer.GeneratedImage(image, width[0].toFloat(), height[0].toFloat())
    }

    // TODO: Convert units from Float to Int in actual constructor

    override fun registerImage(image: String, width: Float, height: Float, flags: Int, buffer: ByteBuffer) {
        images[image] = nvgCreateImageRGBA(ctx, width.toInt(), height.toInt(), flags, buffer)
    }

    override fun deleteImage(image: String) {
        nvgDeleteImage(ctx, images[image] ?: return)
    }

    var rasterizer: Long = 0L

    override fun rasterizeSVG(buffer: ByteBuffer, scale: Float): UIRenderer.GeneratedImage {
        val svg: NSVGImage
        MemoryStack.stackPush().use {
            val copy = ByteBuffer.allocateDirect(buffer.capacity())
            buffer.rewind()
            copy.put(buffer)
            buffer.rewind()
            copy.flip()

            svg =
                NanoSVG.nsvgParse(copy, it.ASCII("px"), 96f) ?: throw RuntimeException("Failed to parse the given SVG.")
        }
        // Create the rasterizer if necessary
        if (rasterizer == 0L)
            rasterizer = NanoSVG.nsvgCreateRasterizer()

        val width = (svg.width() * scale).toInt()
        val height = (svg.height() * scale).toInt()
        val rast = MemoryUtil.memAlloc(width * height * 4)

        NanoSVG.nsvgRasterize(rasterizer, svg, 0f, 0f, scale, rast, width, height, width * 4)

        return UIRenderer.GeneratedImage(rast, svg.width(), svg.height())
    }

    override fun createFont(fontName: String, fontData: ByteBuffer?): Boolean {
        if (fontData == null) {
            println("Failed to load font $fontName as it was not found.")
            return false
        }
        nvgCreateFontMem(ctx, fontName, fontData, 0)
        return true
    }

    override fun deleteFont(fontName: String) {
        throw RuntimeException("NanoVG does not support deleting fonts. Please opt to load fonts once instead.")
    }

    override fun imagePattern(
        imageName: String, x: Float, y: Float, width: Float, height: Float, angle: Float, alpha: Float
    ) = imagePattern(images[imageName] ?: 0, x, y, width, height, angle, alpha)

    override fun imagePattern(
        imageHandle: Int,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        angle: Float,
        alpha: Float
    ) {
        allocPaint()
        nvgImagePattern(
            ctx, x, y, width, height, angle, imageHandle, alpha, paint
        )
        paint.innerColor(fillColor)
        paint.outerColor(fillColor)
    }

    override fun fontFace(fontName: String) = nvgFontFace(ctx, fontName)

    override fun fontSize(size: Float) = nvgFontSize(ctx, size)

    override fun fontSpacing(spacing: Float) = nvgTextLetterSpacing(ctx, spacing)

    override fun fontAlignment(horizontalAlignment: UITextAlignment, verticalAlignment: UITextAlignment) {
        var alignment = when (horizontalAlignment) {
            UITextAlignment.LEFT -> NVG_ALIGN_LEFT
            UITextAlignment.CENTER -> NVG_ALIGN_CENTER
            UITextAlignment.RIGHT -> NVG_ALIGN_RIGHT
            else -> 0
        }
        alignment = alignment or when (verticalAlignment) {
            UITextAlignment.TOP -> NVG_ALIGN_TOP
            UITextAlignment.CENTER -> NVG_ALIGN_MIDDLE
            UITextAlignment.BOTTOM -> NVG_ALIGN_BOTTOM
            else -> 0
        }
        nvgTextAlign(ctx, alignment)
    }

    override fun renderText(text: String, x: Float, y: Float) {
        nvgFillColor(ctx, fillColor)
        nvgText(ctx, x, y, text)
        nvgTextMetrics(ctx, ascender, descender, null)
        nvgFontFace(ctx, "Montserrat-Regular")
        fontBounds[4] = nvgTextBounds(ctx, x, y, text, fontBounds)
//        println(fontBounds)
    }

    override fun renderText(text: ArrayList<String>, x: Float, y: Float, lineHeight: Float) {
        var offset = y
        var minx = x
        var miny = y
        var maxx = 0f
        var maxy = 0f

        for (i in text.indices) {
            renderText(text[i], x, offset)
            minx = fontBounds[0].coerceAtMost(minx)
            miny = fontBounds[1].coerceAtMost(miny)
            maxx = fontBounds[2].coerceAtLeast(maxx)
            maxy = fontBounds[3].coerceAtLeast(maxy)
            offset += fontBounds[3] - fontBounds[1] + lineHeight
        }

        fontBounds[0] = minx
        fontBounds[1] = miny
        fontBounds[2] = maxx
        fontBounds[3] = maxy
        fontBounds[4] = maxx
    }

    val rows = NVGTextRow.create(50)

    override fun renderText(
        text: String,
        x: Float,
        y: Float,
        lineWidth: Float,
        lineHeight: Float,
        lines: ArrayList<String>?
    ): Int {
        val nrows = nvgTextBreakLines(ctx, text, lineWidth, rows)

        var offset = y
        var minx = x
        var miny = y
        var maxx = 0f
        var maxy = 0f

        for (i in 0 until nrows) {
            val row = rows[i]

            lines?.add(MemoryUtil.memUTF8(row.start(), (row.end() - row.start()).toInt()))
            nnvgTextBounds(ctx, x, offset, row.start(), row.end(), fontBounds)
            nnvgText(ctx, x, offset, row.start(), row.end())

            minx = fontBounds[0].coerceAtMost(minx)
            miny = fontBounds[1].coerceAtMost(miny)
            maxx = fontBounds[2].coerceAtLeast(maxx)
            maxy = fontBounds[3].coerceAtLeast(maxy)

            offset += fontBounds[3] - fontBounds[1] + lineHeight
        }

        fontBounds[0] = minx
        fontBounds[1] = miny
        fontBounds[2] = maxx
        fontBounds[3] = maxy
        fontBounds[4] = maxx

        return nrows
    }

    override fun calculateText(text: ArrayList<String>, lineHeight: Float) {
        var offset = 0f
        var minx = -1f
        var miny = -1f
        var maxx = 0f
        var maxy = 0f

        for (i in text.indices) {
            nvgTextBounds(ctx, 0f, offset, text[i], fontBounds)
            minx = fontBounds[0].coerceAtMost(minx)
            miny = fontBounds[1].coerceAtMost(miny)
            maxx = fontBounds[2].coerceAtLeast(maxx)
            maxy = fontBounds[3].coerceAtLeast(maxy)
            offset += fontBounds[3] - fontBounds[1] + lineHeight
        }

        fontBounds[0] = minx
        fontBounds[1] = miny
        fontBounds[2] = maxx
        fontBounds[3] = maxy
        fontBounds[4] = maxx
    }

    override fun calculateText(
        text: String,
        lineWidth: Float,
        lineHeight: Float,
        lines: ArrayList<String>?
    ): Int {
        val nrows = nvgTextBreakLines(ctx, text, lineWidth, rows)

        var offset = 0f

        var minx = -1f
        var miny = -1f
        var maxx = 0f
        var maxy = 0f

        for (i in 0 until nrows) {
            val row = rows[i]

            lines?.add(MemoryUtil.memUTF8(row.start(), (row.end() - row.start()).toInt()))
            nnvgTextBounds(ctx, 0f, offset, row.start(), row.end(), fontBounds)

            minx = fontBounds[0].coerceAtMost(minx)
            miny = fontBounds[1].coerceAtMost(miny)
            maxx = fontBounds[2].coerceAtLeast(maxx)
            maxy = fontBounds[3].coerceAtLeast(maxy)

            offset += fontBounds[3] - fontBounds[1] + lineHeight
        }

        fontBounds[0] = minx
        fontBounds[1] = miny
        fontBounds[2] = maxx
        fontBounds[3] = maxy
        fontBounds[4] = maxx

        nvgTextMetrics(ctx, ascender, descender, null)

        return nrows
    }

    override fun fontBounds(): FloatArray = fontBounds

    override fun fontBounds(text: String): FloatArray {
        nvgTextMetrics(ctx, ascender, descender, null)
        fontBounds[4] = nvgTextBounds(ctx, 0f, 0f, text, fontBounds)
        return fontBounds
    }

    override fun fontAscender(): Float = ascender[0]

    override fun fontDescender(): Float = -descender[0]

    override fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) = nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)

    override fun ellipse(x: Float, y: Float, xRadius: Float, yRadius: Float) = nvgEllipse(ctx, x, y, xRadius, yRadius)

    override fun circle(x: Float, y: Float, radius: Float) = nvgCircle(ctx, x, y, radius)

    override fun beginPath() = nvgBeginPath(ctx)

    override fun closePath() = nvgClosePath(ctx)

    override fun fill() {
        nvgFill(ctx)
        deallocatePaint()
    }

    override fun fillPaint() {
        nvgFillPaint(ctx, paint ?: throw NullPointerException("Attempted to fill paint without an active paint"))
        fill()
    }

    override fun fillColor(color: Int) = color(color)

    override fun stroke() {
        nvgStroke(ctx)
        deallocatePaint()
    }

    override fun strokePaint() {
        nvgStrokePaint(ctx, paint ?: throw NullPointerException("Attempted to stroke paint without an active paint"))
        stroke()
    }

    override fun strokeWidth(size: Float) = nvgStrokeWidth(ctx, size)

    override fun strokeColor(color: Int) {
        nvgColor(color, strokeColor)
        nvgStrokeColor(ctx, strokeColor)
    }

    override fun pathHole(value: Boolean) = nvgPathWinding(ctx, if (value) NVG_HOLE else NVG_SOLID)

    override fun moveTo(x: Float, y: Float) = nvgMoveTo(ctx, x, y)

    override fun lineTo(x: Float, y: Float) = nvgLineTo(ctx, x, y)

    override fun bezierTo(cx: Float, cy: Float, cx2: Float, cy2: Float, x: Float, y: Float) =
        nvgBezierTo(ctx, cx, cy, cx2, cy2, x, y)

    override fun quadTo(cx: Float, cy: Float, x: Float, y: Float) = nvgQuadTo(ctx, cx, cy, x, y)

    override fun arc(
        x: Float, y: Float, radius: Float, startAngle: Float, endAngle: Float, windingOrder: UIRenderer.WindingOrder
    ) = nvgArc(
        ctx, x, y, radius, startAngle, endAngle, if (windingOrder == UIRenderer.WindingOrder.CW) NVG_CW else NVG_CCW
    )

    override fun arcTo(x: Float, y: Float, x1: Float, y1: Float, radius: Float) = nvgArcTo(ctx, x, y, x1, y1, radius)

    override fun lineCap(cap: UIRenderer.LineCap) = nvgLineCap(
        ctx, when (cap) {
            UIRenderer.LineCap.Butt -> NVG_BUTT
            UIRenderer.LineCap.Round -> NVG_ROUND
            UIRenderer.LineCap.Square -> NVG_SQUARE
        }
    )

    override fun lineJoin(join: UIRenderer.LineJoin) = nvgLineJoin(
        ctx, when (join) {
            UIRenderer.LineJoin.Miter -> NVG_MITER
            UIRenderer.LineJoin.Round -> NVG_ROUND
            UIRenderer.LineJoin.Bevel -> NVG_BEVEL
        }
    )

    override fun linearGradient(x: Float, y: Float, x2: Float, y2: Float, startColor: Int, endColor: Int) {
        allocPaint()
        nvgColor(startColor, gradient1)
        nvgColor(endColor, gradient2)
        nvgLinearGradient(ctx, x, y, x2, y2, gradient1, gradient2, paint!!)
    }

    override fun radialGradient(
        x: Float, y: Float, innerRadius: Float, outerRadius: Float, startColor: Int, endColor: Int
    ) {
        allocPaint()
        nvgColor(startColor, gradient1)
        nvgColor(endColor, gradient2)
        nvgRadialGradient(ctx, x, y, innerRadius, outerRadius, gradient1, gradient2, paint!!)
    }

    override fun allocPaint() {}

    override fun deallocatePaint() {
        paint.image(0)
        paint.feather(0f)
        paint.radius(0f)
    }

    override fun degToRad(deg: Float): Float = nvgDegToRad(deg)

    override fun radToDeg(rad: Float): Float = nvgRadToDeg(rad)

    override fun resizeImage(
        buffer: ByteBuffer,
        initialWidth: Int,
        initialHeight: Int,
        expectedWidth: Int,
        expectedHeight: Int
    ): ByteBuffer {
        // Used for UIImage for resizing textures not equal to the
        // normal bounds. See if for more information.

        // Allocate the new buffer with the new width * height * 4 for a total of 32 bytes per pixel.
        val resizedImage: ByteBuffer = MemoryUtil.memAlloc(expectedWidth * expectedHeight * 4)

        STBImageResize.stbir_resize_uint8_generic(
            buffer, initialWidth, initialHeight, initialWidth * 4,
            resizedImage, expectedWidth, expectedHeight, expectedWidth * 4,
            4, 3, STBImageResize.STBIR_FLAG_ALPHA_PREMULTIPLIED,
            STBImageResize.STBIR_EDGE_CLAMP,
            // Use the Mitchel filter.
            STBImageResize.STBIR_FILTER_MITCHELL,
            STBImageResize.STBIR_COLORSPACE_SRGB
        )

        return resizedImage
    }

    private fun nvgColor(color: Int, nvgColor: NVGColor) {
        nvgRGBA(
            color.red.toByte(),
            color.green.toByte(),
            color.blue.toByte(),
            color.alpha.toByte(),
            nvgColor
        )
    }
}