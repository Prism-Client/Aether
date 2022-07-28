package net.prismclient.aether.ui.util.shorthands

fun Int.getRed(): Int = this shr 16 and 0xFF

fun Int.getGreen(): Int = this shr 8 and 0xFF

fun Int.getBlue(): Int = this and 0xFF

fun Int.getAlpha(): Int = this shr 24 and 0xFF

fun asRGBA(r: Int, g: Int, b: Int, a: Int = 255) =
    r shl 16 or (g shl 8) or b or (a shl 24)

fun asRGBA(r: Float, g: Float, b: Float, a: Float = 1f): Int =
    asRGBA((r * 255 + 0.5f).toInt(), (g * 255 + 0.5f).toInt(), (b * 255 + 0.5f).toInt(), (a * 255 + 0.5f).toInt())

fun asRGBA(r: Int, g: Int, b: Int, a: Float) = asRGBA(r, g, b, (a * 255 + 0.5).toInt())

fun Int.limitRange(): Int =
    asRGBA(this.getRed().limit(), this.getGreen().limit(), this.getBlue().limit(), this.getAlpha().limit())

fun Int.limit(): Int = this.coerceAtMost(255).coerceAtLeast(0)

fun Float.limit(): Float = this.coerceAtMost(1f).coerceAtLeast(0f)