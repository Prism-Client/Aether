package net.prismclient.aether.ui.input

import net.prismclient.aether.core.event.type.*

/**
 * [UIInput] contains the functions used for handling different input changes, such as the mouse
 * moving and scrolling. Internally, Compositions already implement the functions and also publish
 * the event corresponding to it.
 *
 * @author sen
 * @since 1.0
 */
interface UIInput {
    fun mouseMoved(event: MouseMove)

    /**
     * A propagating event which is invoked when the mouse is pressed and this is
     * the innermost composable within the mouse, or a parent of it.
     */
    fun mousePressed(event: MousePress)

    fun mouseReleased(event: MouseReleased)


    fun mouseScrolled(event: MouseScrolled)

    fun keyPressed(event: KeyPressed)


    // TODO: Other events eg Mouse enter/leave
    // TODO: Focusable objects
}