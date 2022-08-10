package net.prismclient.aether.ui.layout.util

/**
 * Indicates the order to iterate through the children list of a layout.
 *
 * @author sen
 * @since 1.0
 */
enum class LayoutOrder {
    /**
     * The first child is laid first, and the last is laid last.
     */
    FIRST,

    /**
     * The last child is laid first, and the first child is laid last.
     */
    LAST
}