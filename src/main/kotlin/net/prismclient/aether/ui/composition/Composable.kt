package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.util.shorthands.px

/**
 * [Composable] is an object which Aether uses to compose the UI. In other words, it is a class
 * which has position and size properties and is played out within a composition.
 *
 * @author sen
 * @since 1.0
 */
abstract class Composable {
    var parent: Composable? = null

    /**
     * Returns the width of [parent], or the width of the display.
     */
    val parentWidth: Float
        get() = if (parent != null) parent!!.parentWidth else Aether.instance.displayWidth

    /**
     * Returns the height of [parent], or the height of the display.
     */
    val parentHeight: Float
        get() = if (parent != null) parent!!.parentHeight else Aether.instance.displayHeight

    /**
     * Returns true if this has been composed at least once.
     */
    var composed: Boolean = false

    /**
     * Returns true if this or a child (but not sub-children) have a relative/dynamic unit.
     */
    var dynamic: Boolean = false

    var x: UIUnit<*>? = null
    var y: UIUnit<*>? = null
    var width: UIUnit<*>? = null
    var height: UIUnit<*>? = null
    var anchorPoint: AnchorPoint? = null

    open fun updatePosition() {
        x?.compute(this, false)
        y?.compute(this, false)
    }

    open fun updateSize() {
        width?.compute(this, false)
        height?.compute(this, false)
    }

    /**
     * Invoked when this is to be composed. Units and other properties should be initialized at this point.
     */
    abstract fun compose()

    /**
     * Invoked when this should be rendered.
     */
    abstract fun render()
}

/**
 * Adjusts the x of this to the given [value].
 */
fun Composable.x(value: UIUnit<*>) = apply {
    x = value
}

/**
 * Adjusts the y of this to the given [value].
 */
fun Composable.y(value: UIUnit<*>) = apply {
    y = value
}

/**
 * Adjusts the width of this to the given [value].
 */
fun Composable.width(value: UIUnit<*>) = apply {
    width = value
}

/**
 * Adjusts the height of this to the given [value].
 */
fun Composable.height(value: UIUnit<*>) = apply {
    height = value
}

/**
 * Adjusts the position of this Composable to the given [x] and [y] coordinate units.
 */
fun Composable.position(x: UIUnit<*>, y: UIUnit<*>) = apply {
    this.x = x
    this.y = y
}

/**
 * Adjusts the position of this Composable to the given [x] and [y] coordinate values.
 */
fun Composable.position(x: Number, y: Number) = position(x.px, y.px)

/**
 * Adjusts the size of this Composable to the given [width] and [height] units.
 */
fun Composable.size(width: UIUnit<*>, height: UIUnit<*>) = apply {
    this.width = width
    this.height = height
}

/**
 * Adjusts the size of this Composable to the given [width] and [height] values.
 */
fun Composable.size(width: Number, height: Number) = size(width.px, height.px)

/**
 * Constrains this to be within the bounds of the given units.
 */
fun Composable.constrain(x: UIUnit<*>, y: UIUnit<*>, width: UIUnit<*>, height: UIUnit<*>) = apply {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}

/**
 * Constrains this to be within the bounds of the given values.
 */
fun Composable.constrain(x: Number, y: Number, width: Number, height: Number) =
    constrain(px(x), px(y), px(width), px(height))