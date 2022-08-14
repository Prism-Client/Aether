package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.layout.UILayout
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.core.util.shorthands.dp

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
     * Returns the x position of the [parent] or 0.
     */
    open val parentX: Float get() = parent?.x ?: 0f

    /**
     * Returns the y position of the [parent] or 0.
     */
    open val parentY: Float get() = parent?.y ?: 0f

    /**
     * Returns the width of [parent], or the width of the composition.
     */
    open val parentWidth: Float get() = parent?.width ?: composition.width

    /**
     * Returns the height of [parent], or the height of the composition.
     */
    open val parentHeight: Float get() = parent?.height ?: composition.height

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

        println(modifier.x)

        if (!overridden) {
            x = modifier.x.dp - modifier.anchorPoint?.x.dp + parentX
            y = modifier.y.dp - modifier.anchorPoint?.y.dp + parentY
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
        width = modifier.width.dp
        height = modifier.height.dp
    }

    /**
     * Updates the relative x, y, width and height of this based on the padding.
     */
    open fun composeBounds() {
        relX = x - modifier.padding?.left.dp
        relY = y - modifier.padding?.top.dp
        relWidth = width + modifier.padding?.right.dp + modifier.padding?.left.dp
        relHeight = height + modifier.padding?.bottom.dp + modifier.padding?.top.dp
    }

    open fun composeAnchor() {
        modifier.anchorPoint?.update(this, width, height)
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
