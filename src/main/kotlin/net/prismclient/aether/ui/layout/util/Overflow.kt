package net.prismclient.aether.ui.layout.util

import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.layout.scroll.Scrollbar

/**
 * Indicates how a scrollbar acts within a layout. The behavior nearly is identical to Web/CSS overflow. See the
 * respected enums for more information.
 *
 * @author sen
 * @since 1.0
 * @see UILayout
 * @see Scrollbar
 * @see LayoutModifier
 */
enum class Overflow {
    /**
     * Does nothing; the scrollbar is not introduced, and the content is not clipped
     */
    VISIBLE,

    /**
     * Clips any content flowing outside the bounds of the layout.
     *
     * Note: The default renderer, NanoVG does not support multiple scissor operations, so any scissor calls prior
     * to this will be ignored. (Such as the scissor calls when Compositions have optimizations disabled).
     */
    HIDDEN,

    /**
     * Introduces a scrollbar. The [Overflow.HIDDEN] state applies.
     */
    SCROLLBAR,

    /**
     * Introduces a scrollbar **ONLY** if the content overflows (exceeds) the layout. The [Overflow.HIDDEN] state applies.
     */
    AUTO
}