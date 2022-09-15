package net.prismclient.aether.core.animation

import net.prismclient.aether.core.ease.UIEase
import net.prismclient.aether.core.util.property.Animatable
import net.prismclient.aether.core.util.shorthands.copy
import java.lang.NullPointerException

/**
 * @author sen
 * @since 1.0
 */
abstract class Animation<T : Animatable<T>> {
    open var context: AnimationContext<T>? = null

    open var keyframes: ArrayList<Keyframe> = arrayListOf()
    open var activeKeyframe: Keyframe? = null
    open var nextKeyframe: Keyframe? = null

    open var completionAction: Runnable? = null

    open var animating = false
        protected set

    open fun start() {
        if (keyframes.isEmpty()) throw IllegalStateException("No keyframes to animate")

        activeKeyframe = keyframes[0]
        nextKeyframe = keyframes.getOrNull(1)

        activeKeyframe!!.ease.start()

        animating = true
    }

    open fun next() {
        activeKeyframe = nextKeyframe
        nextKeyframe = keyframes.getOrNull(keyframes.indexOf(activeKeyframe) + 1)
        activeKeyframe?.ease?.start()

        if (activeKeyframe == null || nextKeyframe == null) {
            complete()
            return
        }
    }

    open fun complete() {
        completionAction?.run()
    }

    open fun update(obj: T) {
        val context = context ?: AnimationContext(obj.copy)

        if (activeKeyframe == null || nextKeyframe == null) {
            complete()
            return
        }

        if (activeKeyframe!!.ease.finished) {
            next()
            context.updateContext(obj.copy)
            if (activeKeyframe == null || nextKeyframe == null) {
                complete()
                return
            }
        }

        obj.animate(
            context,
            context.snapshot,
            activeKeyframe?.animatable,
            nextKeyframe?.animatable,
            (activeKeyframe?.ease?.getValue() ?: 0.0).toFloat()
        )
    }

    open fun createKeyframe(ease: UIEase, animatable: T) = Keyframe(ease, animatable)

    inner class Keyframe(val ease: UIEase, val animatable: T)
}