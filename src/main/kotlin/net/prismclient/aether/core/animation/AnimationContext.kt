package net.prismclient.aether.core.animation

import net.prismclient.aether.core.util.property.Animatable

/**
 * [AnimationContext] stores the state of the current animation.
 *
 * @author sen
 * @since 1.0
 */
class AnimationContext<T : Animatable<T>>(snapshot: T?) {
    /**
     * [snapshot] represents a copy of the active [Animatable], [T]. It is intended to
     * store the initial values of an animation so that if the given property is null,
     * the property will reflect the initial value.
     */
    var snapshot: T? = snapshot

    var changesLayout: Boolean = false

    /**
     * Resets the active context and changes the [snapshot].
     */
    fun updateContext(snapshot: T?) {
        this.snapshot = snapshot
        changesLayout = false
    }

    /**
     * Indicates to the active animation that the property change requires the composition
     * to be recomposed.
     */
    fun recompose() {
        changesLayout = true
    }
}