package net.prismclient.aether.core.animation

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier
import kotlin.reflect.KClass

/**
 * @author sen
 * @since 1.0
 */
abstract class Animation<C : Composable> {
    var keyframes: ArrayList<Keyframe<*>> = arrayListOf()
    var activeKeyframe: Keyframe<*>? = null
    var nextKeyframe: Keyframe<*>? = null

    var animating = false

    fun start() {
        println("Starting the animation... ")
        if (keyframes.isEmpty()) throw IllegalStateException("No keyframes to animate")

        activeKeyframe = keyframes[0]
        nextKeyframe = keyframes.getOrNull(1)

        activeKeyframe!!.ease.start()

        animating = true
    }

    fun next() {
        println("Next")
        activeKeyframe = nextKeyframe
        nextKeyframe = keyframes.getOrNull(keyframes.indexOf(activeKeyframe) + 1)
        activeKeyframe?.ease?.start()
    }

    fun complete() {
        println("Completed the animation!")
    }

    fun update(composable: Composable) {
        if (activeKeyframe == null || nextKeyframe == null) {
            complete()
            return
        }

        if (activeKeyframe!!.ease.finished) {
            next()
            if (activeKeyframe == null || nextKeyframe == null) {
                complete()
                return
            }
        }
        

//        composable.modifier.animate(
//            activeKeyframe?.modifier,
//            activeKeyframe?.modifier,
//            (activeKeyframe?.ease?.getValue() ?: 0.0).toFloat()
//        )
    }
}