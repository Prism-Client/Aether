package net.prismclient.aether.ui.composition

import net.prismclient.aether.core.event.UIEvent
import net.prismclient.aether.core.event.UIEventBus
import net.prismclient.aether.core.event.*
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.registry.register
import net.prismclient.aether.ui.unit.UIUnit
import java.util.function.Consumer
import kotlin.math.roundToInt
import kotlin.reflect.KClass

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

    protected open var compositionRef: Composition? = null

    /**
     * The composition which this composable is assigned to. The functions within ComponentsKt class
     * automatically set this value. If nothing is set it will default to the default composition.
     */
    open var composition: Composition
        get() {
//            if (compositionRef == null)
//                compositionRef = Aether.instance.defaultComposition
            return compositionRef!!
        }
        set(value) {
            compositionRef = value
        }
    open var parent: Composable? = null

    /**
     * The events of a [Composable] it is automatically allocated if necessary
     */
    open var events: HashMap<KClass<out UIEvent>, HashMap<String, Consumer<out UIEvent>>>? = null

    /**
     * Returns true if this has been composed at least once.
     */
    open var composed: Boolean = false

    /**
     * Returns true if this or a child (but not sub-children) has a relative/dynamic unit.
     */
    open var dynamic: Boolean = false

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

    /**
     * Updates the anchor point and computes the [UIModifier.x] and [UIModifier.y] values and sets them to [x] and [y].
     */
    open fun composePosition() {
        composeAnchor()
        modifier.x?.compute(false)
        modifier.y?.compute(true)

        if (!overridden) {
            x = (modifier.x.dp - modifier.anchorPoint?.x.dp + parentX()).roundToInt().toFloat()
            y = (modifier.y.dp - modifier.anchorPoint?.y.dp + parentY()).roundToInt().toFloat()
        }
        composeBounds()
    }

    /**
     * Updates the size of this and sets the calculated properties to [width] and [height]. If the
     * units are dynamic, [Composable.dynamic] will be true. The padding is calculated.
     */
    open fun composeSize() {
        composePadding()
        modifier.width?.compute(false)
        modifier.height?.compute(true)
        width = modifier.width.dp.roundToInt().toFloat()
        height = modifier.height.dp.roundToInt().toFloat()
    }

    /**
     * Updates the relative x, y, width and height of this based on the padding.
     */
    open fun composeBounds() {
        relX = (x - modifier.padding?.left.dp).roundToInt().toFloat()
        relY = (y - modifier.padding?.top.dp).roundToInt().toFloat()
        relWidth = (width + modifier.padding?.right.dp + modifier.padding?.left.dp).roundToInt().toFloat()
        relHeight = (height + modifier.padding?.bottom.dp + modifier.padding?.top.dp).roundToInt().toFloat()
    }

    open fun composeAnchor() {
        modifier.anchorPoint?.update(this, width.toFloat(), height.toFloat())
    }

    open fun composePadding() {
        modifier.padding?.compose(this)
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
        composition.recompose()
    }

    /**
     * Unsafely invokes each listener of the [event].
     */
    open fun <T : UIEvent> publish(event: T) {
        events?.get(event::class)?.forEach {
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
        listenerName: String = "${T::class.simpleName}:${(events?.get(T::class)?.size ?: 0)}",
        allocateEventListener: Boolean = true,
        listener: Consumer<T>
    ) {
        // Allocate the event HashMap if necessary.
        events = events ?: hashMapOf()
        events!!.computeIfAbsent(T::class) {
            // If the given event is absent, allocate it and add
            // the listener mentioned above. Propagating events
            // can be assumed to be handled manually.
            if (allocateEventListener && T::class !is PropagatingEvent) {
                UIEventBus.register<T>(it) { event ->
                    publish(event)
                    recompose()
                }
            }
            HashMap()
        }[listenerName] = listener
    }


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
    open fun parentWidth(): Float = parent?.width ?: composition.width

    /**
     * Returns the height of [parent], or the height of the composition.
     */
    open fun parentHeight(): Float = parent?.height ?: composition.height

    /**
     * Returns true if the given coordinates are within the normal bounds (non relative values) of this.
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
     * Returns true if the given [event] is registered within this composable
     */
    inline fun <reified T : UIEvent> hasEventListener(event: T): Boolean = events?.get(T::class) != null

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