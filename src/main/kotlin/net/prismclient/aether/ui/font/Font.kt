package net.prismclient.aether.ui.font

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.font.TextResizing.*
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.style.Style
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.shorthands.*
import java.util.regex.Pattern

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
open class UIFont(open val style: FontStyle) : ComposableShape(), Copyable<UIFont> {
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
    open val text: ArrayList<String> = arrayListOf()

    /**
     * The actual, unformatted original text. Setting this does not update the text. [updateFont] must be manually invoked.
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
    open val fontMetrics: FloatArray = FloatArray(7)

    open var anchor: AnchorPoint? = null

//    /**
//     * The text to be appended to the string when it is truncated
//     *
//     */
//    var truncatedText: String? = null
//        set(value) {
//            field = value
////            if (value != null) textResizing = TextResizing.TruncateText
//        }

    override fun update(composable: Composable?) {
        composable!!
        // TODO: Ensure the right order of operations
        style.preUpdate(this)
        super.update(composable)
        style.update(composable)
        updateFont()
        anchor?.update(composable, width.dp, height.dp)
    }

    /**
     * Updates [fontMetrics] and [text] to fit the current [FontStyle.textResizing] values. Updates
     * the position and size if necessary based on the [FontStyle.textResizing].
     */
    open fun updateFont() {
        // Calculate the bounds of the text and update text
        text.clear()
        UIRendererDSL.font(style.actualFontName ?: "", style.fontSize.dp, LEFT, TOP, style.fontSpacing.dp)
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
            else -> {} // TODO:
        }
    }

    override fun render() {
        val horizontalAlignment = style.horizontalAlignment ?: LEFT
        val verticalAlignment = style.verticalAlignment ?: TOP
        val width = width.dp
        val height = height.dp
        val fontSize = style.fontSize.dp
        val lineHeight = style.lineHeight.dp

        val x = x.dp + initialX + when (horizontalAlignment) {
            CENTER -> width / 2f
            RIGHT -> width
            else -> 0f
        } - anchor?.x.dp

        val y = y.dp + initialY + when (verticalAlignment) {
            CENTER -> fontSize / 2f + (height - (fontSize * text.size) - (lineHeight * (text.size - 1))) / 2f
            BOTTOM -> fontSize + (height - (fontSize * text.size) - (lineHeight * (text.size - 1)))
            else -> 0f
        } - anchor?.y.dp + fontMetrics[6] / 2f

        renderer {
            color(style.fontColor)
            font(
                style.actualFontName ?: "",
                style.fontSize.dp,
                horizontalAlignment,
                verticalAlignment,
                style.fontSpacing.dp
            )
            when (style.textResizing) {
                AutoWidth -> actualText.render(x, y)
                AutoHeight -> renderer.renderText(text, x, y, lineHeight)
                FixedSize -> renderer.renderText(actualText, x, y, width, lineHeight, null)
                else -> throw NullPointerException("TextResizing of $style cannot be null.")
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
    open var x: UIUnit<*>? = null
    open var y: UIUnit<*>? = null
    open var width: UIUnit<*>? = null
    open var height: UIUnit<*>? = null
    open var anchor: AnchorPoint? = null

    /**
     * The actual font name passed to the renderer. This can be set to a custom font name that is registered
     * within the renderer with [UIRenderer.createFont] or a [fontFamily] can be set.
     */
    open var actualFontName: String? = null

    /**
     * [textResizing] is the equivalent of resizing property of text in Figma.
     *
     * @see textResizing
     */
    open var textResizing: TextResizing? = null
    open var horizontalAlignment: UITextAlignment? = null
    open var verticalAlignment: UITextAlignment? = null

    open var fontFamily: UIFontFamily? = null
    open var fontType: FontType? = null

    open var fontColor: UIColor? = null
    open var fontSize: UIUnit<*>? = null
    open var fontSpacing: UIUnit<*>? = null

    /**
     * The spacing between each line of text.
     */
    open var lineHeight: UIUnit<*>? = null

    /**
     * When true, the text is offset by the descender to align the bottom bounds of the font to
     * the baseline. This makes the font look more like Figma, as that is what it does by default.
     */
    open var offsetBaseline: Boolean = true

    override fun preUpdate(font: UIFont) {
        font.x = x ?: font.x
        font.y = y ?: font.y
        font.width = width ?: font.width
        font.height = height ?: font.height
        font.anchor = anchor ?: font.anchor
    }

    override fun update(composable: Composable?) {
        composable!!
        ifNotNull(fontFamily) {
            println("here")
            actualFontName = "${fontFamily!!.familyName}-${fontType?.name?.lowercase()}"
        }
        fontSize?.compute(composable, composable.width, composable.height, false)
        fontSpacing?.compute(composable, composable.width, composable.height, false)
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
        it.offsetBaseline = offsetBaseline
    }

    override fun merge(other: FontStyle?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
            width = other.width or width
            height = other.height or height
            anchor = other.anchor or anchor
            textResizing = other.textResizing ?: textResizing
            horizontalAlignment = other.horizontalAlignment ?: horizontalAlignment
            verticalAlignment = other.verticalAlignment ?: verticalAlignment
            if (other.actualFontName != null) actualFontName = other.actualFontName
            fontFamily = other.fontFamily ?: fontFamily
            fontType = other.fontType ?: fontType
            fontColor = other.fontColor ?: fontColor
            fontSize = other.fontSize ?: fontSize
            fontSpacing = other.fontSpacing ?: fontSpacing
            lineHeight = other.lineHeight ?: lineHeight
            offsetBaseline = other.offsetBaseline || offsetBaseline
        }
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

/**
 * Sets the fontName of this [FontStyle] to the [name]. This is overwritten if [FontStyle.fontFamily] is not null.
 */
fun FontStyle.fontName(name: String) = apply {
    actualFontName = name
}

/**
 * Aligns the font on the x-axis to teh given [alignment].
 */
fun FontStyle.horizontalAlignment(alignment: UITextAlignment) = apply {
    horizontalAlignment = alignment
}

/**
 * Aligns the font on the y-axis to the given [alignment].
 */
fun FontStyle.verticalAlignment(alignment: UITextAlignment) = apply {
    verticalAlignment = alignment
}

/**
 * Anchors the font to the given [alignment] relative to the Font's size.
 */
fun FontStyle.fontAnchor(alignment: UIAlignment) = apply {
    anchor = anchor ?: AnchorPoint()
    anchor!!.align(alignment)
}

/**
 * Sets the [FontStyle.offsetBaseline] to the value [offset].
 *
 * When true, the text is offset by the descender to align the bottom bounds of the font to
 * the baseline. This makes the font look more like Figma, as that is what it does by default.
 */
@JvmOverloads
fun FontStyle.offsetBaseline(offset: Boolean = true) = apply {
    offsetBaseline = offset
}