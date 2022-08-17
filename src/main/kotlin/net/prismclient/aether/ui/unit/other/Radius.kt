package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.compute
import net.prismclient.aether.core.util.property.UIProperty
import net.prismclient.aether.core.util.shorthands.ifNotNull
import net.prismclient.aether.core.util.shorthands.lerp
import net.prismclient.aether.core.util.shorthands.or
import net.prismclient.aether.core.util.shorthands.px

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
) : UIProperty<Radius> {
    override fun compose(composable: Composable?) {
        composable!!
        topLeft?.compute(composable, true)
        topRight?.compute(composable, false)
        bottomRight?.compute(composable, true)
        bottomLeft?.compute(composable,false)
    }

    override fun copy(): Radius = Radius(topLeft?.copy(), topRight?.copy(), bottomRight?.copy(), bottomLeft?.copy())

    override fun merge(other: Radius?) {
        if (other != null) {
            topLeft = other.topLeft or topLeft
            topRight = other.topRight or topRight
            bottomRight = other.bottomRight or bottomRight
            bottomLeft = other.bottomLeft or bottomLeft
        }
    }

    override fun animate(start: Radius?, end: Radius?, fraction: Float) {
        ifNotNull(start?.topLeft, end?.topLeft) {
            topLeft = topLeft ?: 0.px
            topLeft!!.lerp(topLeft, start?.topLeft, end?.topLeft, fraction)
        }
        ifNotNull(start?.topRight, end?.topRight) {
            topRight = topRight ?: 0.px
            topRight!!.lerp(topRight, start?.topRight, end?.topRight, fraction)
        }
        ifNotNull(start?.bottomRight, end?.bottomRight) {
            bottomRight = bottomRight ?: 0.px
            bottomRight!!.lerp(bottomRight, start?.bottomRight, end?.bottomRight, fraction)
        }
        ifNotNull(start?.bottomLeft, end?.bottomLeft) {
            bottomLeft = bottomLeft ?: 0.px
            bottomLeft!!.lerp(bottomLeft, start?.bottomLeft, end?.bottomLeft, fraction)
        }
    }

    override fun toString(): String =
        "UIRadius(topLeft=$topLeft, topRight=$topRight, bottomRight=$bottomRight, bottomLeft=$bottomLeft)"
}