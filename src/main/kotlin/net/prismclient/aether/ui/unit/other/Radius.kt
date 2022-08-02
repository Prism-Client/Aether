package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.Property
import net.prismclient.aether.ui.util.shorthands.*
import net.prismclient.aether.ui.util.shorthands.ifNotNull
import net.prismclient.aether.ui.util.shorthands.lerp

/**
 * [Radius] represents a 4 corner shape, such as rectangle which can be used to create
 * rounded corners.
 *
 * @author sen
 * @since 1.0
 */
open class Radius(
    var topLeft: UIUnit<*>? = null,
    var topRight: UIUnit<*>? = null,
    var bottomRight: UIUnit<*>? = null,
    var bottomLeft: UIUnit<*>? = null
) : Property<Radius> {
    override fun update(composable: Composable?) {
        composable!!
        topLeft?.compute(composable, composable.width.dp, composable.height.dp, true)
        topRight?.compute(composable, composable.width.dp, composable.height.dp, false)
        bottomRight?.compute(composable, composable.width.dp, composable.height.dp, true)
        bottomLeft?.compute(composable, composable.width.dp, composable.height.dp, false)
    }

    override fun copy(): Radius = Radius(topLeft?.copy(), topRight?.copy(), bottomRight?.copy(), bottomLeft?.copy())

    override fun merge(other: Radius?): Radius {
        if (other == null) return copy()
        return Radius(
            topLeft = other.topLeft or topLeft,
            topRight = other.topRight or topRight,
            bottomRight = other.bottomRight or bottomRight,
            bottomLeft = other.bottomLeft or bottomLeft
        )
    }

    override fun animate(start: Radius?, end: Radius?, fraction: Float) {
        ifNotNull(start?.topLeft, end?.topLeft) {
            topLeft = topLeft ?: 0.px
            topLeft!!.lerp(start?.topLeft, end?.topLeft, fraction)
        }
        ifNotNull(start?.topRight, end?.topRight) {
            topRight = topRight ?: 0.px
            topRight!!.lerp(start?.topRight, end?.topRight, fraction)
        }
        ifNotNull(start?.bottomRight, end?.bottomRight) {
            bottomRight = bottomRight ?: 0.px
            bottomRight!!.lerp(start?.bottomRight, end?.bottomRight, fraction)
        }
        ifNotNull(start?.bottomLeft, end?.bottomLeft) {
            bottomLeft = bottomLeft ?: 0.px
            bottomLeft!!.lerp(start?.bottomLeft, end?.bottomLeft, fraction)
        }
    }

    override fun toString(): String =
        "UIRadius(topLeft=$topLeft, topRight=$topRight, bottomRight=$bottomRight, bottomLeft=$bottomLeft)"
}