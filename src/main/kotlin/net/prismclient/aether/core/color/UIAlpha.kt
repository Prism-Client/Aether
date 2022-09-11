package net.prismclient.aether.core.color

import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.util.property.Animatable
import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.core.util.property.Mergable
import net.prismclient.aether.core.util.shorthands.lerp

/**
 * [UIAlpha] represents a alpha value between 0 and 1.
 *
 * @author sen
 * @since 1.0
 */
class UIAlpha(var value: Float) : Copyable<UIAlpha>, Mergable<UIAlpha>, Animatable<UIAlpha> {
    /**
     * Creates a new alpha value from an Integer varying from the range 0 to 255
     */
    constructor(value: Int) : this((value and 0xFF) / 255f)

    override fun animate(context: AnimationContext<*>, start: UIAlpha?, end: UIAlpha?, progress: Float) {
        TODO("Not yet implemented")
    }

    override fun copy(): UIAlpha = UIAlpha(value)

    override fun merge(other: UIAlpha?) {
        if (other != null) {
            value = other.value
        }
    }

    override fun toString(): String = "UIAlpha($value)"
}