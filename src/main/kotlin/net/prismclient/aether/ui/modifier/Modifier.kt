package net.prismclient.aether.ui.modifier

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.composition.util.color
import net.prismclient.aether.ui.composition.util.radius
import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.ui.renderer.UIColor
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding
import net.prismclient.aether.ui.unit.other.Radius
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.shorthands.px

/**
 * [Modifier] contains information such as the position, background, padding and other properties
 * that reflect the component that this is passed to. [Modifier] is an inheritable class where custom
 * properties can be added to add custom effects if the given API is not enough.
 *
 * @author sen
 * @since 1.0
 */
open class Modifier : Copyable<Modifier> {
    var x: UIUnit<*>? = null
    var y: UIUnit<*>? = null
    var width: UIUnit<*>? = null
    var height: UIUnit<*>? = null

    var padding: Padding? = null
    var margin: Margin? = null

    var background: UIBackground? = null

    open fun preUpdate(component: Composable) {
        component.x = x ?: component.x
        component.y = y ?: component.y
        component.width = width ?: component.width
        component.height = height ?: component.height
    }

    open fun update(component: Composable) {
        background?.update(component)
    }

    /**
     * Invoked prior to the component draw.
     */
    open fun preRender() {
        background?.render()
    }

    /**
     * Invoked after the component draw
     */
    open fun render() {

    }

    override fun copy(): Modifier = Modifier().also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.padding = padding?.copy()
        it.margin = margin?.copy()
        it.background = background?.copy()
    }
}

/**
 * Adjusts the x of this to the given [value].
 */
fun Modifier.x(value: UIUnit<*>) = apply {
    x = value
}

/**
 * Adjusts the y of this to the given [value].
 */
fun Modifier.y(value: UIUnit<*>) = apply {
    y = value
}

/**
 * Adjusts the width of this to the given [value].
 */
fun Modifier.width(value: UIUnit<*>) = apply {
    width = value
}

/**
 * Adjusts the height of this to the given [value].
 */
fun Modifier.height(value: UIUnit<*>) = apply {
    height = value
}

/**
 * Adjusts the position of this modifier to the given [x] and [y] coordinate units.
 */
fun Modifier.position(x: UIUnit<*>, y: UIUnit<*>) = apply {
    this.x = x
    this.y = y
}

/**
 * Adjusts the position of this modifier to the given [x] and [y] coordinate values.
 */
fun Modifier.position(x: Number, y: Number) = position(x.px, y.px)

/**
 * Adjusts the size of this Modifier to the given [width] and [height] units.
 */
fun Modifier.size(width: UIUnit<*>, height: UIUnit<*>) = apply {
    this.width = width
    this.height = height
}

/**
 * Adjusts the size of this Modifier to the given [width] and [height] values.
 */
fun Modifier.size(width: Number, height: Number) = size(width.px, height.px)

/**
 * Constrains this Modifier to be within the bounds of the given units.
 */
fun Modifier.constrain(x: UIUnit<*>, y: UIUnit<*>, width: UIUnit<*>, height: UIUnit<*>) = apply {
    this.x = x
    this.y = y
    this.width = width
    this.height = height
}

/**
 * Constrains this Modifier to be within the bounds of the given values.
 */
fun Modifier.constrain(x: Number, y: Number, width: Number, height: Number) =
    constrain(x.px, y.px, width.px, height.px)

/**
 * Sets the padding of this Modifier to the given units.
 */
fun Modifier.padding(top: UIUnit<*>?, right: UIUnit<*>?, bottom: UIUnit<*>?, left: UIUnit<*>?) = apply {
    padding = Padding(top, right, bottom, left)
}

/**
 * Sets the padding of this Modifier to the given values.
 */
fun Modifier.padding(top: Number, right: Number, bottom: Number, left: Number) =
    padding(top.px, right.px, bottom.px, left.px)

/**
 * Sets the margin of this Modifier to the given units.
 */
fun Modifier.margin(top: UIUnit<*>?, right: UIUnit<*>?, bottom: UIUnit<*>?, left: UIUnit<*>?) = apply {
    margin = Margin(top, right, bottom, left)
}

/**
 * Sets the margin of this Modifier to the given values.
 */
fun Modifier.margin(top: Number, right: Number, bottom: Number, left: Number) =
    margin(top.px, right.px, bottom.px, left.px)

/**
 * Sets the background of this Modifier to the given [color]. A background is allocated if none is set.
 */
fun Modifier.backgroundColor(color: UIColor) = apply {
    background = background ?: UIBackground()
    background!!.color(color)
}

fun Modifier.backgroundRadius(radius: Radius) = apply {
    background = background ?: UIBackground()
    background!!.radius(radius)
}