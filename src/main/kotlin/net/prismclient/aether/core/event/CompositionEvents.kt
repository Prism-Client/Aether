package net.prismclient.aether.core.event

import net.prismclient.aether.core.input.MouseButtonType
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition

/**
 * Indicates an event where a singular composable is invoked, and the event moves (propagates) up the
 * composition tree until the composition. [propagate] informs this to propagate up.
 *
 * //todo: better documentatino for PropagatingEvents
 *
 * @author sen
 * @since 1.0
 */
abstract class PropagatingEvent(val initialComposable: Composable) : CustomEvent(initialComposable) {
    /**
     * The amount of [Composable] (nodes) which this [PropagatingEvent] has passed through; the amount
     * of times this has been propagated
     */
    var propagationIndex: Int = 0

    /**
     * The active [Composable] which is being propagated
     */
    var currentComposable: Composable = initialComposable

    /**
     * The [Composable] which propagated to the [currentComposable] level.
     */
    var previousComposable: Composable = initialComposable

    var requiresRecompose: Boolean = false

    // TODO: Cancellable Propagating Events

    /**
     * Updates the [currentComposable] and [previousComposable] up one node of the tree and publishes this to the new [Composable].
     */
    fun propagate() {
        propagationIndex++
        if (currentComposable is Composition && (currentComposable as Composition).isTopLayer()) {
            if (requiresRecompose) currentComposable.compose()
            return
        }
        previousComposable = currentComposable
        currentComposable = currentComposable.parent ?: currentComposable.composition!!
        currentComposable.publish(this)
    }

    /**
     * Returns true if the [initialComposable] is equal to the [currentComposable], and the [propagationIndex]
     * is 0. In other words, if this is at the first propagation level, this will return true.
     */
    fun isInitial(): Boolean = currentComposable == initialComposable && propagationIndex == 0

    /**
     * Indicates to the composition that this even is going to propagate to that the layout needs to be recomposed.
     */
    override fun recompose() {
        requiresRecompose = true
    }
}

/**
 * Invoked prior to rendering.
 *
 * @see RenderEvent
 */
class PreRenderEvent : UIEvent

/**
 * Invoked after rendering the compositions.
 *
 * @see PreRenderEvent
 */
class RenderEvent : UIEvent

/**
 * Invoked when the mouse is moved. [mouseX] and [mouseY] represent the mouse coordinates.
 */
class MouseMove(val mouseX: Float, val mouseY: Float) : UIEvent

/**
 * A [PropagatingEvent] which holds the [mouseX] and [mouseY] coordinates as well as the [button] pressed. Because
 * this is a [PropagatingEvent], a specific composable is invoked and then the parent of that composable is invoked.
 */
class MousePress(val mouseX: Float, val mouseY: Float, val button: MouseButtonType, composable: Composable) :
    PropagatingEvent(composable)

/**
 * Invoked when the mouse is released. [mouseX] and [mouseY] represent the mouse coordinates, and [button]
 * represents the button of which was released.
 */
class MouseRelease(val mouseX: Float, val mouseY: Float, val button: MouseButtonType) : UIEvent

// MouseScrolled && KeyPressed are within FocusableEvents.kt !!!
// ~ sen ~