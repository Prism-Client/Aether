package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.core.util.property.Animatable
import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.core.util.property.Mergable
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.Alignment.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit

/**
 * Expects a width and height which is used to scale the given properties
 *
 * @author sen
 * @since 1.0
 */
open class AnchorPoint : Copyable<AnchorPoint>, Mergable<AnchorPoint>, Animatable<AnchorPoint> {
    open var x: UIUnit<*>? = null
    open var y: UIUnit<*>? = null

    open fun update(composable: Composable?, width: Float, height: Float) {
        disableDynamicCheck(composable) {
            x?.compute(composable, width, height, false)
            y?.compute(composable, width, height, true)
        }
    }

    /**
     * Sets the x and y values to relative values based on the [alignment]
     */
    fun align(alignment: Alignment) {
        x = when (alignment) {
            TOPCENTER, CENTER, BOTTOMCENTER -> 0.5f
            TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> 1f
            else -> 0f
        }.crel
        y = when (alignment) {
            MIDDLELEFT, CENTER, MIDDLERIGHT -> 0.5f
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> 1f
            else -> 0f
        }.crel
    }

    override fun copy(): AnchorPoint = AnchorPoint().also {
        it.x = x?.copy()
        it.y = y?.copy()
    }

    override fun merge(other: AnchorPoint?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
        }
    }

    override fun animate(start: AnchorPoint?, end: AnchorPoint?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(x, start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(y, start?.y, end?.y, fraction)
        }
    }

    override fun toString(): String = "AnchorPoint($x, $y)"
}