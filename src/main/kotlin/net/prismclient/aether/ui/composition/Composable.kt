package net.prismclient.aether.ui.composition

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.event.UIEvent
import net.prismclient.aether.core.event.UIEventBus
import net.prismclient.aether.core.event.type.MouseMoveEvent
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.shorthands.dp
import net.prismclient.aether.ui.util.shorthands.px
import java.util.function.Consumer

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

    /**
     * The composition which this composable is assigned to. The functions within ComponentsKt class
     * automatically set this value, however if not used, it must be manually assigned.
     */
    open lateinit var composition: Composition
    open var parent: Composable? = null

    /**
     * Returns the origin point of this composable. The origin point is the position of this relative
     * to the position of the composition. When optimize composition is true, the origin point is always
     * the x position of the composition, and not 0, thus this returns an offset.
     */
    open val originX: Float get() = parent?.x ?: if (composition.modifier.optimizeComposition) 0f else composition.x

    /**
     * @see originX
     */
    open val originY: Float get() = parent?.y ?: if (composition.modifier.optimizeComposition) 0f else composition.y

    /**
     * Returns the width of [parent], or the width of the display.
     */
    open val parentWidth: Float get() = if (parent != null) parent?.modifier?.width.dp else Aether.instance.displayWidth

    /**
     * Returns the height of [parent], or the height of the display.
     */
    open val parentHeight: Float get() = if (parent != null) parent?.modifier?.height.dp else Aether.instance.displayHeight

    /**
     * Returns true if this has been composed at least once.
     */
    open var composed: Boolean = false

    /**
     * Returns true if this or a child (but not sub-children) has a relative/dynamic unit.
     */
    open var dynamic: Boolean = false

    open var x: Float = 0f
    open var y: Float = 0f
    open var width: Float = 0f
    open var height: Float = 0f

    /**
     * Updates the anchor point and computes the [UIModifier.x] and [UIModifier.y] values and sets them to [x] and [y].
     */
    open fun updatePosition() {
        updateAnchor()
        modifier.x?.compute(false)
        modifier.y?.compute(true)

        if (!overridden) {
            x = modifier.x.dp - modifier.anchorPoint?.x.dp + originX
            y = modifier.y.dp - modifier.anchorPoint?.y.dp + originY
        }
    }

    /**
     * Updates the size of this and sets the calculated properties to [width] and [height]. If the
     * units are dynamic, [Composable.dynamic] will be true.
     */
    open fun updateSize() {
        modifier.width?.compute(false)
        modifier.height?.compute(true)
        width = modifier.width.dp
        height = modifier.height.dp
    }

    open fun updateAnchor() {
        modifier.anchorPoint?.update(this, modifier.width.dp, modifier.height.dp)
    }

    /**
     * Invoked when this is to be composed. Units and other properties should be initialized at this point.
     */
    abstract fun compose()

    /**
     * Invoked when this should be rendered.
     */
    abstract fun render()

    /**
     * Informs the [Composition] of this [Composable] to recompose (update the layout), and re-rasterize.
     */
    open fun recompose() {
        // TODO: Recompose only necessary elements
//        composition.recompose()
    }

    /**
     * Computes the given unit with the [parentWidth] and [parentHeight].
     */
    @Suppress
    protected fun UIUnit<*>?.compute(yaxis: Boolean) {
        this?.compute(this@Composable, parentWidth, parentHeight, yaxis)
    }
}

// -- Events -- //

//fun <T : UIButton> T.onClick(event: Consumer<MouseMoveEvent>) = apply {
//    UIEventBus.register(event)
//}

///**
// * Wraps the [consumer] within another consumer which recomposes the layout after being invoked. The
// * new consumer is registered to the event bus.
// */
//private inline fun <T : Composable, reified E : UIEvent> T.addRecompose(consumer: Consumer<E>) {
//    UIEventBus.register(Consumer<E> {
//        consumer.accept(it)
//        recompose()
//    })
//}
