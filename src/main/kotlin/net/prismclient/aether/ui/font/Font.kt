package net.prismclient.aether.ui.font

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.debug.UIDebug
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.renderer.UIColor
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.style.Style
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.shorthands.*
import java.util.regex.Pattern
import net.prismclient.aether.ui.font.TextResizing.*

/**
 * [UIFont] is the implementation for a composable that needs a font. It expects a [FontStyle]
 * which dictates the properties of this font.
 *
 * Aether is designed with Figma in mind, and because of that, [UIFont] is designed to mimic the text component
 * within Figma.
 *
 * @author sen
 * @since 1.0
 */
open class UIFont(val style: FontStyle) : ComposableShape(), Copyable<UIFont> {
    override var x: UIUnit<*>? = null
    override var y: UIUnit<*>? = null
    override var width: UIUnit<*>? = null
    override var height: UIUnit<*>? = null

    /**
     * The formatted text. Depending on the [FontStyle.textResizing] it can be one line,
     * or split by the newline character or width.
     *
     * @see actualText
     */
    val text: ArrayList<String> = arrayListOf()

    /**
     * The actual, unformatted original text. Setting this does not
     * update the text. [updateFont] must be manually invoked.
     *
     * @see text
     */
    @set:JvmName("setText")
    var actualText: String = ""

    /**
     * The metrics of the text. It is formatted as
     *
     *      minX - The minimum x coordinate of the text.
     *      minY - The minimum y coordinate of the text.
     *      maxX - The maximum x coordinate of the text.
     *      maxY - The maximum y coordinate of the text.
     *      advance - The x coordinate of the next character's glyph position
     *      ascent - The y coordinate of the top to the descender line.
     *      descent - The y coordinate of the descender line to the bottom of the text's bounding box.
     */
    val fontMetrics: FloatArray = FloatArray(7)

    var anchor: AnchorPoint? = null

//    /**
//     * The text to be appended to the string when it is truncated
//     *
//     */
//    var truncatedText: String? = null
//        set(value) {
//            field = value
////            if (value != null) textResizing = TextResizing.TruncateText
//        }

    override fun update(composable: Composable) {
        style.preUpdate(this)
        super.update(composable)
        updateFont()
        anchor?.update(composable, width.dp, height.dp)
        style.update(composable)
    }

    /**
     * Updates [fontMetrics] and [text] to fit the current [FontStyle.textResizing] values. Updates
     * the position and size if necessary based on the [FontStyle.textResizing].
     */
    open fun updateFont() {
        // Change the mode based on the properties of this and the style if necessary.
        if (style.textResizing == TextResizing.Auto) style.textResizing = AutoWidth
        when {
            /* When the mode is auto (width/height) and the size properties aren't null it is a fixed size. */
            (style.textResizing == AutoWidth || style.textResizing == AutoHeight)
                    && (width != null || height != null) ->
                style.textResizing = FixedSize
            /* When the mode is AutoWidth and contains a newline character */
            style.textResizing == AutoWidth && actualText.contains(NEWLINE) ->
                style.textResizing = AutoHeight
        }

        // Calculate the bounds of the text and update text
        text.clear()
        UIRendererDSL.font(style.actualFontName, style.fontSize.dp, LEFT, TOP, style.fontSpacing.dp)
        when (style.textResizing) {
            AutoWidth -> {
                text.add(actualText)
                actualText.bounds
            }
            AutoHeight -> {
                text.addAll(actualText.split(NEWLINE))
                Aether.renderer.calculateText(text, style.lineHeight.dp)
            }
            FixedSize -> Aether.renderer.calculateText(actualText, width.dp, style.lineHeight.dp, text)
            // TODO: Truncate text
            else -> {}
        }
        // The font metrics are calculated above, and can be referenced from fontBounds().
        // After setting the font bounds, update the ascender and descender values
        fontMetrics[0] = fontBounds().minX
        fontMetrics[1] = fontBounds().minY
        fontMetrics[2] = fontBounds().maxX
        fontMetrics[3] = fontBounds().maxY
        fontMetrics[4] = fontBounds().advance
        fontMetrics[5] = fontAscender()
        fontMetrics[6] = fontDescender()

        when (style.textResizing) {
            AutoWidth, AutoHeight -> {
                width?.cachedValue = fontWidth()
                height?.cachedValue = fontHeight()
            }
            else -> // TODO:
        }
    }

    override fun render() {
        val x = x.dp + when (style.horizontalAlignment) {
            CENTER -> width.dp / 2f
            RIGHT -> width.dp
            else -> 0f
        } - anchor?.x.dp + initialX
        val y = y.dp + when (style.verticalAlignment) {
            CENTER -> style.fontSize.dp / 2f + (height.dp - (style.fontSize.dp * text.size) - (style.lineHeight.dp * (text.size - 1))) / 2f
            BOTTOM -> style.fontSize.dp + (height.dp - (style.fontSize.dp * text.size) - (style.lineHeight.dp * (text.size - 1)))
            else -> 0f
        } - anchor?.y.dp + initialY + if (style.shiftBaseline) fontMetrics[6] / 2f else 0f

        UIDebug.renderBounds(initialX + this.x.dp, initialY + this.y.dp, width.dp, height.dp, UIDebug.font)

        renderer {
            color(style.fontColor)
            font(style.actualFontName, style.fontSize.dp, style.horizontalAlignment, style.verticalAlignment, style.fontSpacing.dp)
            when (style.textResizing) {
                AutoWidth -> actualText.render(x, y)
                AutoHeight -> renderer.renderText(text, x, y, style.lineHeight.dp)
                FixedSize -> renderer.renderText(actualText, x, y, width.dp, style.lineHeight.dp, null)
                else -> {}
            }
        }
    }

    override fun copy(): UIFont = UIFont(style.copy()).also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.actualText = actualText
        it.anchor = anchor?.copy()
    }

    companion object {
        @JvmStatic
        protected val NEWLINE: Regex = Pattern.compile("\\r?\\n|\\r").toRegex()
    }
}

/**
 * Represents the style of a font. For defining the font to use, either a font family should be passed
 * to the property [FontStyle.fontFamily]. Alternatively the name of the font registered with [UIRenderer.createFont]
 * can be set to [FontStyle.actualFontName] instead. Text/Fonts are designed to mimic Figma's properties of text.
 *
 * @see UIFont Font rendering and more documentation.
 * @see TextResizing Figma's resizing property for how text renders.
 *
 * @author sen
 * @since 1.0
 */
open class FontStyle : Style<FontStyle>() {
    var x: UIUnit<*>? = null
    var y: UIUnit<*>? = null
    var width: UIUnit<*>? = null
    var height: UIUnit<*>? = null
    var anchor: AnchorPoint? = null

    /**
     * The actual font name passed to the renderer. This can be set to a custom font name that is registered
     * within the renderer with [UIRenderer.createFont] or a [fontFamily] can be set.
     */
    var actualFontName: String = ""

    /**
     * [textResizing] is the equivalent of resizing property of text in Figma.
     *
     * @see textResizing
     */
    var textResizing: TextResizing = TextResizing.Auto
    var horizontalAlignment: UITextAlignment = CENTER
    var verticalAlignment: UITextAlignment = CENTER

    var fontFamily: UIFontFamily? = null
    var fontType: FontType = FontType.Regular

    var fontColor: UIColor? = null
    var fontSize: UIUnit<*>? = null
    var fontSpacing: UIUnit<*>? = null

    /**
     * The spacing between each line of text.
     */
    var lineHeight: UIUnit<*>? = null

    /**
     * Shifts the y-position by half the baseline to make the text appear more centered when true.
     */
    var shiftBaseline: Boolean = true

    open fun preUpdate(font: UIFont) {
        font.x = x ?: font.x
        font.y = y ?: font.y
        font.width = width ?: font.width
        font.height = height ?: font.height
        font.anchor = anchor ?: font.anchor
    }

    open fun update(composable: Composable) {
        ifNotNull(fontFamily) {
            println("here")
            actualFontName = "${fontFamily!!.familyName}-${fontType.name.lowercase()}"
        }
        fontSize?.compute(composable, false)
        fontSpacing?.compute(composable, false)
    }

    override fun copy(): FontStyle = FontStyle().also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.anchor = anchor?.copy()
        it.textResizing = textResizing
        it.horizontalAlignment = horizontalAlignment
        it.verticalAlignment = verticalAlignment
        it.fontFamily = fontFamily ?: run {
            it.actualFontName = actualFontName
            null
        }
        it.fontType = fontType
        it.fontColor = fontColor?.copy()
        it.fontSize = fontSize?.copy()
        it.fontSpacing = fontSpacing?.copy()
        it.lineHeight = lineHeight?.copy()
        it.shiftBaseline = shiftBaseline
    }

    override fun merge(other: FontStyle?): FontStyle = FontStyle().apply {
        TODO("Feature yet to be implemented")
    }

    override fun animate(start: FontStyle?, end: FontStyle?, fraction: Float) {
        TODO("Feature yet to be implemented")
    }
}

/**
 * Adjusts the x of this to the given [value].
 */
fun FontStyle.x(value: UIUnit<*>) = apply {
    x = value
}

/**
 * Adjusts the y of this to the given [value].
 */
fun FontStyle.y(value: UIUnit<*>) = apply {
    y = value
}

/**
 * Adjusts the width of this to the given [value].
 */
fun FontStyle.width(value: UIUnit<*>) = apply {
    width = value
}

/**
 * Adjusts the height of this to the given [value].
 */
fun FontStyle.height(value: UIUnit<*>) = apply {
    height = value
}

/**
 * Adjusts the position of this FontStyle to the given [x] and [y] coordinate units.
 */
fun FontStyle.position(x: UIUnit<*>, y: UIUnit<*>) = apply {
    this.x = x
    this.y = y
}

/**
 * Adjusts the position of this FontStyle to the given [x] and [y] coordinate values.
 */
fun FontStyle.position(x: Number, y: Number) = position(x.px, y.px)

/**
 * Adjusts the size of this FontStyle to the given [width] and [height] units.
 */
fun FontStyle.size(width: UIUnit<*>, height: UIUnit<*>) = apply {
    this.width = width
    this.height = height
}

/**
 * Adjusts the size of this FontStyle to the given [width] and [height] values.
 */
fun FontStyle.size(width: Number, height: Number) = size(width.px, height.px)

/**
 * Constrains this FontStyle to be within the bounds of the given units.
 */
fun FontStyle.constrain(x: UIUnit<*>, y: UIUnit<*>, width: UIUnit<*>, height: UIUnit<*>) = apply {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}

/**
 * Constrains this FontStyle to be within the bounds of the given values.
 */
fun FontStyle.constrain(x: Number, y: Number, width: Number, height: Number) =
    constrain(x.px, y.px, width.px, height.px)


fun FontStyle.fontFamily(family: UIFontFamily) = apply {
    fontFamily = family
}

fun FontStyle.textResizing(resizing: TextResizing) = apply {
    textResizing = resizing
}

fun FontStyle.fontAlignment(horizontalAlignment: UITextAlignment, verticalAlignment: UITextAlignment) = apply {
    this.horizontalAlignment = horizontalAlignment
    this.verticalAlignment = verticalAlignment
}

fun FontStyle.fontType(type: FontType) = apply {
    fontType = type
}

fun FontStyle.fontColor(color: UIColor) = apply {
    fontColor = color
}

fun FontStyle.fontSize(size: UIUnit<*>) = apply {
    fontSize = size
}

fun FontStyle.fontSpacing(spacing: UIUnit<*>) = apply {
    fontSpacing = spacing
}

fun FontStyle.lineHeight(height: UIUnit<*>) = apply {
    lineHeight = height
}

fun FontStyle.fontName(name: String) = apply {
    actualFontName = name
}


fun FontStyle.horizontalAlignment(alignment: UITextAlignment) = apply {
    horizontalAlignment = alignment
}

fun FontStyle.verticalAlignment(alignment: UITextAlignment) = apply {
    verticalAlignment = alignment
}

fun FontStyle.fontAnchor(alignment: UIAlignment) = apply {
    anchor = anchor ?: AnchorPoint()
    anchor!!.align(alignment)
}