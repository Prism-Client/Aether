package net.prismclient.aether.core.animation.type

import net.prismclient.aether.core.animation.Animation
import net.prismclient.aether.core.event.PreRenderEvent
import net.prismclient.aether.core.event.UIEventBus
import net.prismclient.aether.core.util.property.Animatable
import net.prismclient.aether.ui.composition.Composable
import java.lang.RuntimeException

class ComposableAnimation<T : Animatable<T>> : Animation<T>() {
    var composable: Composable? = null
    var obj: T? = null

    fun start(composable: Composable, obj: T) {
        this.composable = composable
        this.obj = obj
        start()
        UIEventBus.register<PreRenderEvent>(toString()) { update(obj) }
    }

    override fun complete() {
        UIEventBus.unregister<PreRenderEvent>(toString())
        super.complete()
    }

    override fun start() {
        if (composable == null || obj == null) throw RuntimeException("Use start(composable: Composable) instead.")
        super.start()
    }

    override fun update(obj: T) {
        super.update(obj)
        composable!!.composition.compose() // TODO: Hotspot
    }
}