package net.prismclient.aether.ui.font

import net.prismclient.aether.ui.renderer.UIColor
import net.prismclient.aether.ui.style.Style
import net.prismclient.aether.ui.unit.UIUnit

/**
 * Represents a component which contains a font
 *
 * @author sen
 * @since 1.0
 */
open class FontStyle : Style<FontStyle>() {
    /**
     * The actual font name to use when rendering
     */
    var actualFontName: String = ""
        protected set

    var fontFamily: UIFontFamily? = null
    var fontColor: UIColor? = null
    var fontSize: UIUnit<*>? = null
    var fontSpacing: UIUnit<*>? = null

    override fun copy(): FontStyle {
        TODO("Not yet implemented")
    }

    override fun merge(other: FontStyle?): FontStyle = FontStyle().apply {
        fontFamily = other?.fontFamily ?: fontFamily
        fontColor = other?.fontColor ?: fontColor
        fontSize = other?.fontSize ?: fontSize
        fontSpacing = other?.fontSpacing ?: fontSpacing
    }

    override fun animate(start: FontStyle?, end: FontStyle?, fraction: Float) {

    }
}