package net.prismclient.aether.ui.input

import net.prismclient.aether.core.event.type.MousePress

/**
 * [UIInput] contains the functions used for handling different input changes, such as the mouse
 * moving and scrolling. Internally, Compositions already implement the functions and also publish
 * the event corresponding to it.
 *
 * @author sen
 * @since 1.0
 */
interface UIInput {
    fun mouseMoved()

    /**
     * A propagating event which is invoked when the mouse is pressed and this is
     * the innermost composable within the mouse, or a parent of it.
     */
    fun mousePressed(event: MousePress)

    fun mouseReleased()

    fun mouseScrolled()

    fun keyPressed()

    // TODO: Other events eg Mouse enter/leave
    // TODO: Focusable objects
}