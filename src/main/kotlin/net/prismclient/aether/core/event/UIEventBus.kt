package net.prismclient.aether.core.event

import net.prismclient.aether.core.debug.warn
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
    @JvmStatic
    val events: HashMap<KClass<out UIEvent>, ConcurrentHashMap<String, Consumer<out UIEvent>>> = hashMapOf()

    /**
     * Registers the given the [event] to the given event type [T].
     */
    inline fun <reified T : UIEvent> register(eventName: String? = null, event: Consumer<T>) {
        events.computeIfAbsent(T::class) { ConcurrentHashMap() }[eventName ?: event.toString()] = event
    }

    /**
     * The Java version of [register]. Registers the given [event] to the even type [type].
     */
    @JvmStatic
    @JvmOverloads
    fun <T : UIEvent> register(eventName: String? = null, type: Class<out UIEvent>, event: Consumer<T>) {
        events.computeIfAbsent(type.kotlin) { ConcurrentHashMap() }[eventName ?: event.toString()] = event
    }

    /**
     * Unregisters the [eventName] associated with the event, [T]. The event can be unregistered
     * during publishing; however, it will not actually be removed until after the event is published.
     */
    inline fun <reified T : UIEvent> unregister(eventName: String) {
        events[T::class]?.remove(eventName)
    }

    /**
     * The Java version of [unregister]. Unregisters the [eventName] associated with the event,
     * [event]. The event can be unregistered during publishing; however, it will not actually
     * be removed until after the event is published.
     */
    @JvmStatic
    fun unregister(eventName: String, event: Class<out UIEvent>) {
        events[event.kotlin]?.remove(eventName)
    }

    /**
     * Invokes all listeners of the given [event].
     *
     * @see register
     */
    @JvmStatic
    @Suppress("Unchecked_Cast")
    fun <T : UIEvent> publish(event: T) {
        if (events.containsKey(event::class)) {
            events[event::class]!!.forEach { (_, consumer) ->
                try {
                    (consumer as Consumer<T>).accept(event)
                } catch (e: Exception) {
                    warn("Error while publishing event: ${event::class.simpleName}")
                    e.printStackTrace()
                }
            }
        }
    }
}