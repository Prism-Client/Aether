package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.renderer.UIColor

// Colors are in the form of 0xAARRGGBB or ARGB as an Int.

/**
 * Returns the red color of the given color. The provided Int is expected to store the color as RGBA.
 */
@get:JvmName("red")
inline val Int.red: Int get() = this shr 16 and 0xFF

/**
 * Returns the green color of the given color. The provided Int is expected to store the color as RGBA.
 */
@get:JvmName("green")
inline val Int.green: Int get() = this shr 8 and 0xFF

/**
 * Returns the blue color of the given color. The provided Int is expected to store the color as RGBA.
 */
@get:JvmName("blue")
inline val Int.blue: Int get() = this and 0xFF

/**
 * Returns the alpha color of the given color. The provided Int is expected to store the color as RGBA.
 */
@get:JvmName("alpha")
inline val Int.alpha: Int get() = this shr 24 and 0xFF

/**
 * Returns a [UIColor] from the given Int stored in the RGB format. The alpha is assumed to be 255.
 */
// Mask the alpha value and set it to 0xFF (255).
@get:JvmName("rgb")
inline val Number.rgb: UIColor get() = UIColor((this.toInt() and 0x00FFFFFF) or (0xFF shl 24))

/**
 * Returns a [UIColor] from the given Int stored in the RGBA format.
 */
@get:JvmName("rgba")
inline val Number.rgba: UIColor get() = UIColor(this.toInt())

/**
 * Returns an ARGB formatted Int from the given [r], [g], [b] and [a].
 */
@JvmOverloads
fun RGBA(r: Int, g: Int, b: Int, a: Int = 255) =
    (r shl 16) or (g shl 8) or (b) or (a shl 24)

/**
 * Returns an ARGB formatted Int from the given [r], [g], [b] and [a].
 */
@JvmOverloads
fun RGBA(r: Float, g: Float, b: Float, a: Float = 1f): Int =
    RGBA((r * 255 + 0.5f).toInt(), (g * 255 + 0.5f).toInt(), (b * 255 + 0.5f).toInt(), (a * 255 + 0.5f).toInt())

/**
 * Returns an ARGB formatted Int from the given [r], [g], [b] and [a].
 */
fun RGBA(r: Int, g: Int, b: Int, a: Float) = RGBA(r, g, b, (a * 255 + 0.5).toInt())