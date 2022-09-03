package net.prismclient.aether.ui.alignment

/**
 * Represents the alignment of the horizontal and vertical axis.
 *
 * @author sen
 * @since 1.0
 *
 * @see HorizontalAlignment
 * @see VerticalAlignment
 */
@Suppress("Unused", "SpellCheckingInspection")
enum class Alignment {
    TOPLEFT, TOPCENTER, TOPRIGHT,
    MIDDLELEFT, CENTER, MIDDLERIGHT,
    BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT
}

/**
 * Represents alignment on the horizontal (x) axis.
 *
 * @author sen
 * @since 1.0
 *
 * @see Alignment
 * @see VerticalAlignment
 */
enum class HorizontalAlignment {
    LEFT, CENTER, RIGHT
}

/**
 * Represents alignment on the vertical (y) axis.
 *
 * @author sen
 * @since 1.0
 *
 * @see Alignment
 * @see HorizontalAlignment
 */
enum class VerticalAlignment {
    TOP, MIDDLE, BOTTOM
}

/**
 * Returns the [Alignment] version of [HorizontalAlignment]
 */
fun horizontalConvert(alignment: HorizontalAlignment): Alignment = when (alignment) {
    HorizontalAlignment.LEFT -> Alignment.TOPLEFT
    HorizontalAlignment.CENTER -> Alignment.TOPCENTER
    HorizontalAlignment.RIGHT -> Alignment.TOPRIGHT
}

/**
 * Returns the [Alignment] version of [VerticalAlignment]
 */
fun verticalConvert(alignment: VerticalAlignment): Alignment = when (alignment) {
    VerticalAlignment.TOP -> Alignment.TOPLEFT
    VerticalAlignment.MIDDLE -> Alignment.MIDDLELEFT
    VerticalAlignment.BOTTOM -> Alignment.BOTTOMLEFT
}