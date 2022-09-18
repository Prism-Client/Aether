package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.core.animation.AnimationContext
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
open class AnchorPoint : Animatable<AnchorPoint>, Copyable<AnchorPoint>, Mergable<AnchorPoint> {
    open var x: UIUnit<*>? = null
    open var y: UIUnit<*>? = null

    open fun compose(composable: Composable?, width: Float, height: Float) {
        x?.compute(composable, width, height, false)
        y?.compute(composable, width, height, true)
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

    override fun animate(
        context: AnimationContext<*>,
        initial: AnchorPoint?,
        start: AnchorPoint?,
        end: AnchorPoint?,
        progress: Float,
        completed: Boolean
    ) {
        TODO("Not yet implemented")
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
    override fun toString(): String = "AnchorPoint($x, $y)"
}