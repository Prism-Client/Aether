package net.prismclient.aether.core.event

import net.prismclient.aether.ui.composition.Composable

/**
 * [CustomEvent] is used to indicate to Aether to disable automatically registering
 * a global listener. Behind the scenes, when an event first is registered, an event
 * is registered to the global event bus so that it can be listened as
 *
 *      You -> Global Eventbus -> Composable Listeners
 *
 * This process automatically happens to all events the first time they are registered
 * to any Composable. However, in certain cases, such as with [PropagatingEvent]s, it is
 * handled in such a way that does not require this. Implementing this interface informs
 * Aether that the event is not intended to automatically allocate that listener.
 *
 * PLEASE NOTE THE [Composable.recompose] SHOULD BE INVOKED AFTER THE EVENT COMPLETION.
 *
 *
 * @author sen
 * @since 1.0
 */
interface CustomEvent

// TODO: Convert to annotation