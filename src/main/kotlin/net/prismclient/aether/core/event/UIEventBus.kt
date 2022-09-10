package net.prismclient.aether.core.event

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import kotlin.reflect.KClass

/**
 * [UIEventBus] handles all events within Aether. This may vary from user input to state events such as a change
 * within a component. The structure of this event bus follows the general concept, where an event is registered,
 * and somewhere the event is published, along with all of its data which the registered events can listen.
 *
 * @author sen, Decencies
 * @since 1.0
 */
object UIEventBus {
    val events: ConcurrentHashMap<KClass<out UIEvent>, HashMap<String, Consumer<out UIEvent>>> = ConcurrentHashMap()

    /**
     * Registers the given the [event] to the given event type [T].
     *
     * @return If the event was successfully registered.
     */
    inline fun <reified T : UIEvent> register(eventName: String? = null, event: Consumer<T>) {
        events.computeIfAbsent(T::class) { HashMap() }[eventName ?: event.toString()] = event
    }

    /**
     * The Java version of [register]. Registers the given [event] to the even type [type].
     *
     * @return If the event was successfully registered.
     */
    fun <T : UIEvent> register(eventName: String? = null, type: Class<out UIEvent>, event: Consumer<T>) {
        events.computeIfAbsent(type.kotlin) { HashMap() }[eventName ?: event.toString()] = event
    }

    /**
     * Invokes all listeners of the given [event].
     *
     * @see register
     */
    @Suppress("Unchecked_Cast")
    fun <T : UIEvent> publish(event: T) {
        if (events.containsKey(event::class)) {
            events[event::class]!!.forEach { consumer ->
                try {
                    (consumer as Consumer<T>).accept(event)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}