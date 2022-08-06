package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.shorthands.dp
import net.prismclient.aether.ui.util.shorthands.px

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
    open var parent: Composable? = null

    /**
     * Returns the width of [parent], or the width of the display.
     */
    val parentWidth: Float get() = if (parent != null) parent?.modifier?.width.dp else Aether.instance.displayWidth

    /**
     * Returns the height of [parent], or the height of the display.
     */
    val parentHeight: Float get() = if (parent != null) parent?.modifier?.height.dp else Aether.instance.displayHeight

    /**
     * Returns true if this has been composed at least once.
     */
    var composed: Boolean = false

    /**
     * Returns true if this or a child (but not sub-children) has a relative/dynamic unit.
     */
    var dynamic: Boolean = false

    var x: Float = 0f
    var y: Float = 0f
    var width: Float = 0f
    var height: Float = 0f

    /**
     * Updates the anchor point and computes the [UIModifier.x] and [UIModifier.y] values and sets them to [x] and [y].
     */
    open fun updatePosition() {
        updateAnchor()
        modifier.x?.compute(false)
        modifier.y?.compute(true)
        x = modifier.x.dp - modifier.anchorPoint?.x.dp
        y = modifier.y.dp - modifier.anchorPoint?.y.dp
    }

    /**
     * Updates the size of this and sets them to [width] and [height]. If the units are dynamic, [Composable.dynamic] will be true.
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
     * Computes the given unit with the [parentWidth] and [parentHeight].
     */
    @Suppress
    protected fun UIUnit<*>?.compute(yaxis: Boolean) {
        this?.compute(this@Composable, parentWidth, parentHeight, yaxis)
    }
}

/**
 * Adjusts the x of this to the given [value].
 */
fun Composable.x(value: UIUnit<*>) = apply {
    modifier.x = value
}

/**
 * Adjusts the y of this to the given [value].
 */
fun Composable.y(value: UIUnit<*>) = apply {
    modifier.y = value
}

/**
 * Adjusts the width of this to the given [value].
 */
fun Composable.width(value: UIUnit<*>) = apply {
    modifier.width = value
}

/**
 * Adjusts the height of this to the given [value].
 */
fun Composable.height(value: UIUnit<*>) = apply {
    modifier.height = value
}

/**
 * Adjusts the position of this Composable to the given [x] and [y] coordinate units.
 */
fun Composable.position(x: UIUnit<*>, y: UIUnit<*>) = apply {
    modifier.x = x
    modifier.y = y
}

/**
 * Adjusts the position of this Composable to the given [x] and [y] coordinate values.
 */
fun Composable.position(x: Number, y: Number) = position(x.px, y.px)

/**
 * Adjusts the size of this Composable to the given [width] and [height] units.
 */
fun Composable.size(width: UIUnit<*>, height: UIUnit<*>) = apply {
    modifier.width = width
    modifier.height = height
}

/**
 * Adjusts the size of this Composable to the given [width] and [height] values.
 */
fun Composable.size(width: Number, height: Number) = size(width.px, height.px)

/**
 * Constrains this to be within the bounds of the given units.
 */
fun Composable.constrain(x: UIUnit<*>, y: UIUnit<*>, width: UIUnit<*>, height: UIUnit<*>) = apply {
    modifier.x = x
    modifier.y = y
    modifier.width = width
    modifier.height = height
}

/**
 * Constrains this to be within the bounds of the given values.
 */
fun Composable.constrain(x: Number, y: Number, width: Number, height: Number) =
    constrain(x.px, y.px, width.px, height.px)