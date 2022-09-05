package net.prismclient.aether.core.util.shorthands

import net.prismclient.aether.core.color.UIColor

/**
 * ColorKt provides a set of shorthands for obtaining information and creating colors. Colors
 * internally are stored as a UIColor, and within are stored as an Int formatted as ARGB or
 * 0xAARRGGBB / 0xRRGGBB in hex. See UIColor for more documentation on this choice.
 *
 * @author sen
 * @since 1.0
 */

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
 * Returns a [UIColor] from the given Int stored in the RGB format. The alpha is assumed to be 255. Use
 * this instead of [Number.rgba] for hex values, as the alpha is considered to be 0.
 */
// Mask the alpha value and set it to 0xFF (255).
@get:JvmName("rgb")
inline val Number.rgb: UIColor get() = UIColor((this.toInt() and 0x00FFFFFF) or (0xFF shl 24))

/**
 * Returns a [UIColor] from the given Int stored in the RGBA format. For hex values use [Number.rgb] as the alpha
 * will be 255 instead of 0.
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

/**
 * Creates a [UIColor] from the given [r], [g], [b] and [a] represented as a value between 0 and 255 each.
 */
@JvmOverloads
fun ColorOf(r: Int, g: Int, b: Int, a: Int = 255) = RGBA(r, g, b, a).rgba

/**
 * Creates a [UIColor] from the given [r], [g], [b] and [a] represented as a value between 0 and 1 each.
 */
@JvmOverloads
fun ColorOf(r: Float, g: Float, b: Float, a: Float = 1f) = RGBA(r, g, b, a).rgba

/**
 * Creates a [UIColor] from the given [r], [g], [b] represented as a value between 0 and 255 each and
 * [a] represented as a value between 0 and 1.
 */
fun ColorOf(r: Int, g: Int, b: Int, a: Float) = RGBA(r, g, b, a).rgba

// TODO: RGB(A) functions for UIColor
// fun cRGBA(r: Float, g: Float, b: Float, a: Float = 1f): UIColor = RGBA(r, g, b, a).rgb