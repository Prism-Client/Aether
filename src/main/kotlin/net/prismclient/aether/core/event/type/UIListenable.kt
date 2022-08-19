package net.prismclient.aether.core.event.type

import net.prismclient.aether.core.event.UIEvent
import net.prismclient.aether.core.util.other.MouseButtonType
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.util.input.UIKey

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

    // todo: Cancelling
}

/**
 * A type of event which indicates that the only composable invoked is the focused component. Events like keyPress
 * and scrolling are a [FocusedEvent] because only a singular listener should listen.
 */
abstract class FocusedEvent : UIEvent

class MouseMove(val mouseX: Float, val mouseY: Float) : UIEvent

/**
 * A [PropagatingEvent] which holds the [mouseX] and [mouseY] coordinates as well as the [button] pressed. Because
 * this is a [PropagatingEvent], a specific composable is invoked and then the parent of that composable is invoked.
 *
 * @author sen
 * @since 1.0
 */
class MousePress(val mouseX: Float, val mouseY: Float, val button: MouseButtonType, composable: Composable) : PropagatingEvent(composable)

/**
 * Invoked when the mouse is released. [mouseX] and [mouseY] represent the mouse coordinates, and [button]
 * represents the button of which was released.
 *
 * @author sen
 * @since 1.0
 */
class MouseReleased(val mouseX: Float, val mouseY: Float, val button: MouseButtonType) : UIEvent

/**
 * A [FocusedEvent] which invokes the focused composable. [dstX] and [dstY] represents the distance
 * the mouse or trackpad was scrolled in the given axis.
 *
 * @author sen
 * @since 1.0
 */
class MouseScrolled(val dstX: Float, val dstY: Float) : FocusedEvent()

/**
 * A [FocusedEvent] which invokes the focused composable. If the key cannot be mapped to a character,
 * [character] will equal to '\u0000'.
 *
 * @author sen
 * @since 1.0
 */
class KeyPressed(val character: Char, val key: UIKey) : FocusedEvent()