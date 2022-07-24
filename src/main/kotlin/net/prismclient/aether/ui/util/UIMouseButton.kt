package net.prismclient.aether.ui.util

/**
 * [UIMouseButton] represents the buttons on the mouse.
 *
 * @author sen
 * @since 1.0
 */
enum class UIMouseButton {
    /**
     * None of the given mouse buttons. Pass this when the mouse is moved.
     */
    None,

    /**
     * The left button of the mouse, generally represented as 0.
     */
    Primary,

    /**
     * The scroll / middle button, generally represented as 1.
     */
    Middle,

    /**
     * The right button of the mouse, generally represented as 2.
     */
    Secondary
}