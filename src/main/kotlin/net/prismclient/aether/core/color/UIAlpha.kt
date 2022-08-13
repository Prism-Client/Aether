package net.prismclient.aether.core.color

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.util.other.Animatable
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.other.Mergable
import net.prismclient.aether.ui.util.other.Property
import net.prismclient.aether.ui.util.shorthands.ifNotNull
import net.prismclient.aether.ui.util.shorthands.lerp

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

    override fun animate(start: UIAlpha?, end: UIAlpha?, fraction: Float) {
        ifNotNull(start, end) {
            value = lerp(start?.value ?: value, end?.value ?: value, fraction)
        }
    }

    override fun copy(): UIAlpha = UIAlpha(value)

    override fun merge(other: UIAlpha?) {
        if (other != null) {
            value = other.value
        }
    }

    override fun toString(): String = "UIAlpha($value)"
}