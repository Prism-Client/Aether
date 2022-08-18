package net.prismclient.aether.core.event.type

import net.prismclient.aether.core.event.UIEvent
import net.prismclient.aether.ui.composition.Composable

/**
 * Indicates an event where a singular composable is invoked, and the event moves (propagates) up the
 * composition tree until the composition. [propagate] informs this to propagate up.
 *
 * @author sen
 * @since 1.0
 */
abstract class PropagatingEvent(val initialComposable: Composable) : UIEvent {
    var currentComposable: Composable = initialComposable
    var previousComposable: Composable = initialComposable

    fun propagate() {
        previousComposable = currentComposable
        currentComposable = currentComposable.parent ?: currentComposable.composition
        // TODO: When peak recompose
    }
}

/**
 * Invoked when the mouse is moved. [actualX] and [actualY] represents the actual x and y
 * coordinates of the mouse relative to the window. Non-relative coordinates are composable
 * coordinates of the actual mouse position offset by the composable's composition position
 */
class MouseMoveEvent(val actualX: Float, val actualY: Float) : UIEvent

class MousePress(val mouseX: Float, val mouseY: Float, composable: Composable) : PropagatingEvent(composable)

//interface UIMouseMoveListener {
//    fun onMouseMove(mouseX: Float, mouseY: Float)
//}
//
//interface UIMouseClickListener {
//    fun onMouseClick(mouseX: Float, mouseY: Float, mouseButton: Int)
//}
//
//interface UIMouseReleaseListener {
//    fun onMouseRelease(mouseX: Float, mouseY: Float, mouseButton: Int)
//}
//
//interface UIMouseScrollListener {
//    fun onMouseScroll(scrollAmount: Float)
//}
//
