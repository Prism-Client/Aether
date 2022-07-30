package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.Animatable
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.shorthands.ifNotNull
import net.prismclient.aether.ui.util.shorthands.lerp
import net.prismclient.aether.ui.util.shorthands.px
import net.prismclient.aether.ui.util.shorthands.rel

/**
 * Expects a width and height which is used to scale the given properties
 *
 * @author sen
 * @since 1.0
 */
open class AnchorPoint : Copyable<AnchorPoint>, Animatable<AnchorPoint> {
    open var x: UIUnit<*>? = null
    open var y: UIUnit<*>? = null

    /**
     * Sets the x and y values to relative values based on the [alignment]
     */
    fun align(alignment: UIAlignment) {
        x = 0.rel
        y = 0.rel
        x!!.value = when (alignment) {
            UIAlignment.TOPCENTER, UIAlignment.CENTER, UIAlignment.BOTTOMCENTER -> 0.5f
            UIAlignment.TOPRIGHT, UIAlignment.MIDDLERIGHT, UIAlignment.BOTTOMRIGHT -> 1f
            else -> 0f
        }
        y!!.value = when (alignment) {
            UIAlignment.MIDDLELEFT, UIAlignment.CENTER, UIAlignment.MIDDLERIGHT -> 0.5f
            UIAlignment.BOTTOMLEFT, UIAlignment.BOTTOMCENTER, UIAlignment.BOTTOMRIGHT -> 1f
            else -> 0f
        }
    }

    fun update(composable: Composable?, width: Float, height: Float) {
        x?.compute(composable, width, height, false)
        y?.compute(composable, width, height, true)
    }

    override fun copy(): AnchorPoint = AnchorPoint().also {
        it.x = x?.copy()
        it.y = y?.copy()
    }

    override fun animate(start: AnchorPoint?, end: AnchorPoint?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(start?.y, end?.y, fraction)
        }
    }
}