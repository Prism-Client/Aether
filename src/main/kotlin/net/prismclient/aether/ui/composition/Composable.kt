package net.prismclient.aether.ui.composition

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.animation.Animation
import net.prismclient.aether.core.event.*
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit
import java.util.function.Consumer
import kotlin.math.roundToInt
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * [Composable] is the superclass for all UI objects within Aether. Anything that extends this class
 * is considered a composable object, which layouts and positioning are applied to. [Composable]s are
 * intended to be placed within composition and layouts.
 *
 * [Composable] accepts a [UIModifier], which holds the properties of the composable. When extending this
 * class the Modifier functions must be manually invoked.
 *
 * @author sen
 * @since 1.0
 *
 * @see UIModifier
 */
abstract class Composable(open val modifier: UIModifier<*>) {
    /**
     * Overridden is intended to be controlled externally. It is used to indicate whether the
     * position properties of this are changed based on an external source, such as [UILayout].
     */
    open var overridden: Boolean = false

    open var composition: Composition? = Aether.instance.defaultComposition

    open var parent: Composable? = null

    /**
     * The events of a [Composable] it is automatically allocated if necessary
     */
    open var listeners: HashMap<KClass<out UIEvent>, HashMap<String, Consumer<out UIEvent>>>? = null

    /**
     *
     */
    open var animations: HashMap<String, Animation<*>>? = null

    /**
     * Returns true if this has been composed at least once.
     */
    open var composed: Boolean = false

    // The actual positions calculated based on the different properties, such as
    // the position and padding of the Composable. The relative units represent the
    // bounds of the composable (padding applied) which most content scales to.

    open var x: Float = 0f
    open var y: Float = 0f
    open var width: Float = 0f
    open var height: Float = 0f

    open var relX: Float = 0f
    open var relY: Float = 0f
    open var relWidth: Float = 0f
    open var relHeight: Float = 0f

    open fun composePosition() {
        if (!overridden) {
            x = (modifier.x.dp - modifier.anchorPoint?.x.dp + parentX()).roundToInt().toFloat()
            y = (modifier.y.dp - modifier.anchorPoint?.y.dp + parentY()).roundToInt().toFloat()
        }
        composeBounds()
    }

    open fun composeSize() {
        width = modifier.width.dp.roundToInt().toFloat()
        height = modifier.height.dp.roundToInt().toFloat()
    }

    open fun composeBounds() {
        relX = (x - modifier.padding?.left.dp).roundToInt().toFloat()
        relY = (y - modifier.padding?.top.dp).roundToInt().toFloat()
        relWidth = (width + modifier.padding?.right.dp + modifier.padding?.left.dp).roundToInt().toFloat()
        relHeight = (height + modifier.padding?.bottom.dp + modifier.padding?.top.dp).roundToInt().toFloat()
    }

    /**
     * todo be doced
     *
     */
    abstract fun compose()

    /**
     * todo be doced
     */
    abstract fun render()

    /**
     * todo be doced
     */
    open fun recompose() {
        // TODO: Determine when to recompose only necessary elements if necesssary
        composition?.recompose()
    }

    /**
     * Unsafely invokes each listener of the [event].
     */
    open fun <T : UIEvent> publish(event: T) {
        listeners?.get(event::class)?.forEach {
            @Suppress("UNCHECKED_CAST")
            (it.value as Consumer<T>).accept(event)
        }
        if (event is PropagatingEvent) event.propagate()
    }

    /**
     * Adds the listener [listener] with the key as [listenerName] to the event, [T]. When [allocateEventListener] is
     * true, an event listener will be added to [UIEventBus] to listen to the event. If the event is a propagating event
     * it is likely that Aether is already manually handling the event, and the listener does not need ot be added.
     */
    inline fun <reified T : UIEvent> addListener(
        listenerName: String = "${T::class.simpleName}:${(listeners?.get(T::class)?.size ?: 0)}",
        allocateEventListener: Boolean = true,
        listener: Consumer<T>
    ) {
        // Allocate the event HashMap if necessary.
        listeners = listeners ?: hashMapOf()
        listeners!!.computeIfAbsent(T::class) {
            // If the given event is absent, allocate it and add that event
            // to the global eventbus if allocateEventListener is true, and
            // it is not a subclass of CustomEvent, as CustomEvents automatically
            // handle publishing and composing itself.

            if (allocateEventListener && !T::class.isSubclassOf(CustomEvent::class)) {
                UIEventBus.register<T>(listenerName) {
                    publish(it)
//                    recompose()
                }
            }
            HashMap()
        }[listenerName] = listener
    }

    /**
     * Returns true if the given [event] is registered within this composable
     */
    fun hasEventListener(event: UIEvent): Boolean = listeners?.get(event::class) != null


    // -- Util -- //

    /**
     * Returns the x position of the [parent] or 0.
     */
    open fun parentX(): Float = parent?.x ?: 0f

    /**
     * Returns the y position of the [parent] or 0.
     */
    open fun parentY(): Float = parent?.y ?: 0f

    /**
     * Returns the width of [parent], or the width of the composition.
     */
    open fun parentWidth(): Float = parent?.width ?: composition?.width ?: 0f

    /**
     * Returns the height of [parent], or the height of the composition.
     */
    open fun parentHeight(): Float = parent?.height ?: composition?.height ?: 0f

    /**
     * Returns the value required to make the [Composable]'s position values relative to the
     * actual window instead of the Composable. It is also used to offset the mouse for scrollbars.
     */
    open fun xOffset(): Float = parent?.xOffset() ?: 0f

    /**
     * Returns the value required to make the [Composable]'s position values relative to the
     * actual window instead of the Composable. It is also used to offset the mouse for scrollbars.
     */
    open fun yOffset(): Float = parent?.yOffset() ?: 0f

    /**
     * Returns the mouseX position offset by the layout scrollbar (if applicable).
     */
    open fun mouseX(): Float = Aether.instance.mouseX + (parent?.xOffset() ?: 0f)

    /**
     * Returns the mouseY position offset by the layout scrollbar (if applicable).
     */
    open fun mouseY(): Float = Aether.instance.mouseY + (parent?.yOffset() ?: 0f)

    /**
     * Returns true if the mouse is within the normal bounds (non-relative values) of this.
     */
    open fun mouseWithin(): Boolean = isWithin(mouseX(), mouseY())

    /**
     * Returns true if the mouse is within the bounds (relative values) of this.
     */
    open fun mouseWithinBounds(): Boolean = isWithinBounds(mouseX(), mouseY())

    /**
     * Returns true if the given coordinates are within the normal bounds (non-relative values) of this.
     *
     * @see isWithinBounds
     */
    open fun isWithin(xBound: Float, yBound: Float): Boolean =
        (x <= xBound && y <= yBound && x + width >= xBound && y + height >= xBound)

    /**
     * Returns true if the given coordinates are within the bounds (relative values) of this.
     *
     * @see isWithin
     */
    open fun isWithinBounds(xBound: Float, yBound: Float): Boolean =
        (relX <= xBound && relY <= yBound && relX + relWidth >= xBound && relY + relHeight >= yBound)

    /**
     * Computes the given unit with the [parentWidth] and [parentHeight].
     */
    @Suppress
    protected fun UIUnit<*>?.compute(yaxis: Boolean) {
        this?.compute(this@Composable, parentWidth(), parentHeight(), yaxis)
    }
}

// -- Events -- //

fun <T : Composable> T.onClick(eventListener: Consumer<MousePress>) = apply {
    addListener(listener = eventListener)
}