package net.prismclient.aether.core.animation

import net.prismclient.aether.core.event.PreRenderEvent
import net.prismclient.aether.core.event.UIEventBus
import net.prismclient.aether.core.event.UIEventBus.register
import net.prismclient.aether.core.event.UIEventBus.unregister
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier
import kotlin.reflect.KClass

/**
 * @author sen
 * @since 1.0
 */
abstract class Animation<C : Composable, M : UIModifier<M>> {
    var keyframes: ArrayList<Keyframe<M>> = arrayListOf()
    var activeKeyframe: Keyframe<M>? = null
    var nextKeyframe: Keyframe<M>? = null

    var animating = false

    lateinit var composable: Composable

    open fun start(composable: Composable) {
        println("Starting the animation... ")
        this.composable = composable
        if (keyframes.isEmpty()) throw IllegalStateException("No keyframes to animate")

        activeKeyframe = keyframes[0]
        nextKeyframe = keyframes.getOrNull(1)

        activeKeyframe!!.ease.start()

        animating = true

        register<PreRenderEvent>(toString()) { update() }
    }

    open fun next() {
        println("Next")
        activeKeyframe = nextKeyframe
        nextKeyframe = keyframes.getOrNull(keyframes.indexOf(activeKeyframe) + 1)
        activeKeyframe?.ease?.start()
    }

    open fun complete() {
        println("Completed the animation!")
        unregister<PreRenderEvent>(toString())
    }

    open fun update() {
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
        

        (composable.modifier as M).animate(
            activeKeyframe?.modifier as M,
            activeKeyframe?.modifier as M,
            (activeKeyframe?.ease?.getValue() ?: 0.0).toFloat()
        )
    }
}