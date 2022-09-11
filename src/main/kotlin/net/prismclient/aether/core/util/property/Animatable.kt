package net.prismclient.aether.core.util.property

import net.prismclient.aether.core.animation.AnimationContext

/**
 * [Animatable] is an interface of which indicates an object which can be animated
 * given two of itself and a progress value. [T] represents the class of which this
 * interface is implemented. The [Copyable] must also be implemented when implementing
 * this. It is used to create "snapshots" a.k.a. initial values for the animation.
 *
 * @author sen
 * @since 1.0
 */
interface Animatable<T> : Copyable<T> {
    /**
     * Linearly interpolates (lerp) the properties of this with the given [start] and
     * [end] based on the [progress].
     *
     * If a property is changed of which the layout needs to be recomposed, invoke
     * [AnimationContext.recompose] to indicate this.
     */
    fun animate(context: AnimationContext<*>, start: T?, end: T?, progress: Float)
}