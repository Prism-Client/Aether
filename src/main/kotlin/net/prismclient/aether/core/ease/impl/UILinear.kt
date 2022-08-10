package net.prismclient.aether.core.ease.impl

import net.prismclient.aether.core.ease.UIEase
import net.prismclient.aether.core.ease.UIEaseDirection

class UILinear(duration: Long = 1000L, animationDirection: UIEaseDirection = UIEaseDirection.INOUT) :
    UIEase(duration, animationDirection) {
    override fun getValue(): Double = get().toDouble()

    override fun copy(): UIEase = UILinear(duration, animationDirection)
}