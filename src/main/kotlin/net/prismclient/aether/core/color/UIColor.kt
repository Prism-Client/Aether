package net.prismclient.aether.core.color

import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.util.property.Animatable
import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.core.util.property.Mergable
import net.prismclient.aether.core.util.shorthands.*
import java.awt.Color

/**
 * [UIColor] represents an RGBA color. Behind the scenes, the value is represented as an Int
 * where the bytes are stored as ARGB with 1 byte per channel respectively. It also provides
 * utility functions to convert the color into different formats.
 *
 * The color could be stored as an Int, as the color stores exactly enough bytes to support a
 * 32-bit color, but, because of how animations are designed, the color property must be nullable.
 * Theoretically, the Int could be represented as it's class type, Integer, however there is
 * overhead to boxing and unboxing the integer. Using a wrapper avoids the boxing and unboxing,
 * reducing the overhead, though probably slower than having it as a normal Int. However, because
 * it is wrapped in a class, certain optimizations can now be used especially when converting to HSV,
 * as the values can be cached, thus reducing overhead from calculations.
 *
 * @author sen
 * @since 1.0
 */
class UIColor(color: Int) : Copyable<UIColor>, Mergable<UIColor>, Animatable<UIColor> {
    /**
     * Creates the [UIColor] from an HSV color type.
     */
    constructor(hue: Float, saturation: Float, brightness: Float) : this(Color.HSBtoRGB(hue, saturation, brightness))

    var rgba: Int = color
        set(value) {
            field = value
            // Update the HSV value if it is not null
            if (hsv != null)
                Color.RGBtoHSB(rgba.red, rgba.green, rgba.blue, hsv)
        }
    var hsv: FloatArray? = null

    fun getHSV(): FloatArray {
        if (hsv == null) {
            hsv = FloatArray(3)
            Color.RGBtoHSB(rgba.red, rgba.green, rgba.blue, hsv)
        }
        return hsv!!
    }

    fun getRed() = rgba.red

    fun getGreen() = rgba.green

    fun getBlue() = rgba.blue

    fun getAlpha() = rgba.alpha

    override fun copy(): UIColor = UIColor(rgba)

    override fun merge(other: UIColor?) {
        if (other != null) {
            rgba = other.rgba
        }
    }

    override fun animate(
        context: AnimationContext<*>,
        initial: UIColor?,
        start: UIColor?,
        end: UIColor?,
        progress: Float
    ) {
        rgba = colorLerp(start?.rgba ?: initial?.rgba ?: 0, end?.rgba ?: initial?.rgba ?: 0, progress)
    }

    override fun toString(): String =
        "UIColor(color=$rgba, hsv=${hsv.contentToString()}, r:${rgba.red}, g:${rgba.green}, b:${rgba.blue}, a:${rgba.alpha})"
}