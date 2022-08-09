package net.prismclient.aether.core.event.type

import net.prismclient.aether.core.event.UIEvent

/**
 * Invoked when the mouse is moved. [actualX] and [actualY] represents the actual x and y
 * coordinates of the mouse relative to the window. Non-relative coordinates are composable
 * coordinates of the actual mouse position offset by the composable's composition position
 */
class MouseMoveEvent(val actualX: Float, val actualY: Float) : UIEvent

class MousePressEvent(val actualX: Float, val actualY: Float) : UIEvent

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
