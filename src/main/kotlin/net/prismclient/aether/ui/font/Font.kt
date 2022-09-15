package net.prismclient.aether.ui.font

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment.*
import net.prismclient.aether.ui.composer.ComposableContext
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.font.FontType.*
import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.style.Style
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.AnchorPoint
import java.util.regex.Pattern

/**
 * [Font] is an interface which a composable can implement to indicate that it has a [font].
 *
 * @author sen
 * @since 1.0
 */
interface Font {
    val font: UIFont
}

/**
 * [UIFont] is the implementation for a composable that needs a font. It expects a [FontStyle]
 * which dictates the properties of this font. Depending on the property [FontStyle.fontType]
 *
 * Aether is designed with Figma in mind, and because of that, [UIFont] is designed to mimic the text component
 * within Figma.
 *
 * @author sen
 * @since 1.0
 */
@Suppress("LeakingThis")
open class UIFont(open val style: FontStyle) : ComposableShape<Composable>(), Copyable<UIFont> {
    override var x: UIUnit<*>? = null
    override var y: UIUnit<*>? = null
    override var width: UIUnit<*>? = null
    override var height: UIUnit<*>? = null

    /**
     * The formatted text. Depending on the [FontStyle.fontType] it can be one line,
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

    init {
        style.font = this
    }

    /**
     * Expects [composeSize] to be invoked prior to this. This should be invoked after the size
     * and position of the composable have been calculated.
     */
    override fun compose(context: ComposableContext) {
        style.preCompose()
        super.compose(context)
        anchor?.compose(context, width.dp, height.dp)
    }

    /**
     * Attempts to resize the [Composable] based on the metrics of the font.
     */
    open fun composeSize(context: ComposableContext) {
        style.compose(context)
        calculateMetrics()
        when (style.textResizing) {
            AutoWidth -> {
                width = AutoResize()
                height = AutoResize()
                width!!.cachedValue = fontWidth()
                height!!.cachedValue = fontHeight()

                // Update the size of the composable
                context.composable?.width = x.dp + width.dp
                context.composable?.height = y.dp + height.dp
            }
            AutoHeight -> {
                height = AutoResize()
                height!!.cachedValue = fontHeight()

                // Update the height of the composable
                context.composable?.height = y.dp + height.dp
            }
            else -> {}
        }
    }

    /**
     * Determines the [fontMetrics] of this based on the style, type, and [actualText].
     */
    open fun calculateMetrics() {
        // Change the text resizing based if null or a width or height is present
        style.textResizing = style.textResizing ?: FixedSize
        if (style.textResizing == AutoWidth && width != null && width !is AutoResize) {
            style.textResizing = AutoHeight
        }
        if (style.textResizing == AutoHeight && height != null && height !is AutoResize) {
            style.textResizing = FixedSize
        }

        text.clear()
        // Calculate the bounds of the text based on the type and update the actual text.
        Renderer {
            font(style.actualFontName ?: "", style.fontSize.dp, LEFT, TOP, style.fontSpacing.dp)
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
        }

        // Update the fontMetrics after calculating them based on the type
        fontMetrics[0] = fontBounds().minX
        fontMetrics[1] = fontBounds().minY
        fontMetrics[2] = fontBounds().maxX
        fontMetrics[3] = fontBounds().maxY
        fontMetrics[4] = fontBounds().advance
        fontMetrics[5] = fontAscender()
        fontMetrics[6] = fontDescender()

//        println("Calculated metrics as: ${fontMetrics.contentToString()}")
    }

    override fun render() {
        val horizontalAlignment = style.horizontalAlignment ?: LEFT
        val verticalAlignment = style.verticalAlignment ?: TOP
        val width = width.dp
        val height = height.dp
        val lineHeight = style.lineHeight.dp

        val x = x.dp + initialX + when (horizontalAlignment) {
            CENTER -> (width - fontMetrics.maxX - fontMetrics.minY) / 2f
            RIGHT -> (width - fontMetrics.maxX - fontMetrics.minY)
            else -> 0f
        } - anchor?.x.dp

        val y = y.dp + initialY + when (verticalAlignment) {
            CENTER -> (height - fontMetrics.maxY - fontMetrics.minY) / 2f
            BOTTOM -> (height - fontMetrics.maxY - fontMetrics.minY)
            else -> 0f
        } - anchor?.y.dp + if (style.offsetBaseline) fontMetrics[6] / 2f else 0f

        Renderer {
            color(style.fontColor)
            font(style.actualFontName ?: "", style.fontSize.dp, LEFT, TOP, style.fontSpacing.dp)
            when (style.textResizing) {
                AutoWidth -> actualText.render(x, y)
                AutoHeight -> renderer.renderText(text, x, y, lineHeight)
                FixedSize -> renderer.renderText(actualText, x, y, width, lineHeight, null)
                else -> throw NullPointerException("Text Resizing (Font Type) of $style cannot be null.")
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
 * An internal unit used to indicate if the [UIFont] is set to [FontType.AutoWidth] or [FontType.AutoHeight].
 */
internal class AutoResize : UIUnit<AutoResize>(0f){
    override fun updateCache(context: ComposableContext?, width: Float, height: Float, yaxis: Boolean): Float = cachedValue

    override fun copy(): AutoResize = AutoResize()

    override fun toString(): String = "AutoResize($cachedValue)"
}

/**
 * Represents the style of a font. For defining the font to use, either a font family should be passed
 * to the property [FontStyle.fontFamily]. Alternatively the name of the font registered with [UIRenderer.createFont]
 * can be set to [FontStyle.actualFontName] instead. Text/Fonts are designed to mimic Figma's properties of text.
 *
 * @see UIFont Font rendering and more documentation.
 * @see FontType Figma's resizing property for how text renders.
 *
 * @author sen
 * @since 1.0
 */
open class FontStyle : Style<FontStyle, Composable>() {
    /**
     * The reference to the font class. Font automatically sets it.
     */
    open lateinit var font: UIFont

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
     * [fontType] is the equivalent of resizing property of text in Figma. See [FontType] for documentation.
     */
    open var textResizing: FontType? = null
    open var horizontalAlignment: UITextAlignment? = null
    open var verticalAlignment: UITextAlignment? = null

    open var fontFamily: UIFontFamily? = null
    open var fontFaceType: FontFaceType? = null

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

    override fun preCompose() {
        font.x = x ?: font.x
        font.y = y ?: font.y
        font.width = width ?: font.width
        font.height = height ?: font.height
        font.anchor = anchor ?: font.anchor
    }

    override fun compose(context: ComposableContext) {
        ifNotNull(fontFamily) {
            //println("here")
            actualFontName = "${fontFamily!!.familyName}-${fontFaceType?.name?.lowercase()}"
        }
        fontSize?.compute(context, context.activeComposable().width, context.activeComposable().height, false)
        fontSpacing?.compute(context, context.activeComposable().width, context.activeComposable().height, false)
    }

    override fun animate(context: AnimationContext<*>, start: FontStyle?, end: FontStyle?, progress: Float) {
        TODO("Not yet implemented")
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
        it.fontFaceType = fontFaceType
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
            fontFaceType = other.fontFaceType ?: fontFaceType
            fontColor = other.fontColor ?: fontColor
            fontSize = other.fontSize ?: fontSize
            fontSpacing = other.fontSpacing ?: fontSpacing
            lineHeight = other.lineHeight ?: lineHeight
            offsetBaseline = other.offsetBaseline || offsetBaseline
        }
    }
}

/**
 * Applies the given [style] to this, [T] from [UIRegistry].
 */
fun <T : FontStyle> T.of(style: String): T = apply {
    this.applyStyle(style)
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

fun FontStyle.fontType(resizing: FontType) = apply {
    textResizing = resizing
}

fun FontStyle.fontAlignment(horizontalAlignment: UITextAlignment, verticalAlignment: UITextAlignment) = apply {
    this.horizontalAlignment = horizontalAlignment
    this.verticalAlignment = verticalAlignment
}

fun FontStyle.fontFaceType(type: FontFaceType) = apply {
    fontFaceType = type
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
fun FontStyle.fontAnchor(alignment: Alignment) = apply {
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