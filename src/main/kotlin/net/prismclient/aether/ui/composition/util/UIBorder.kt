package net.prismclient.aether.ui.composition.util

import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.property.Animatable
import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.core.util.property.Mergable
import net.prismclient.aether.core.util.shorthands.copy
import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.renderer.UIStrokeDirection
import net.prismclient.aether.ui.shape.Shape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.UIRadius

/**
 * [UIBorder] is intended to be paired with [UIBackground] to create borders for [Composable]s.
 *
 * The properties of Shape are unused
 *
 * @author sen
 * @since 1.0
 */
open class UIBorder : Shape(), Animatable<UIBorder>, Copyable<UIBorder>, Mergable<UIBorder> {
    private lateinit var background: UIBackground
    /**
     * The width of the border. Represents the x-axis when computing.
     */
    var borderWidth: UIUnit<*>? = null
    var borderColor: UIColor? = null
    var borderRadius: UIRadius? = null
    var borderDirection: UIStrokeDirection? = null

    var initialX: Float = 0f
    var initialY: Float = 0f
    var initialWidth: Float = 0f
    var initialHeight: Float = 0f

    open fun compose(background: UIBackground) {
        this.background = background
        borderWidth?.compute(null, background.width.dp, background.height.dp, false)
        initialX = background.x.dp + background.initialX
        initialY = background.y.dp + background.initialY
        initialWidth = background.width.dp
        initialHeight = background.height.dp
    }

    override fun render() {
        Renderer {
            stroke(borderWidth.dp, borderColor?.rgba ?: 0, borderDirection ?: UIStrokeDirection.CENTER) {
                rect(initialX, initialY, initialWidth, initialHeight, background.backgroundRadius)
            }
        }
    }

    override fun animate(context: AnimationContext<*>, initial: UIBorder?, start: UIBorder?, end: UIBorder?, progress: Float) {
        TODO("Not yet implemented")
    }

    override fun copy(): UIBorder = UIBorder().also {
        it.borderWidth = borderWidth.copy
        it.borderColor = borderColor.copy
        it.borderDirection = borderDirection ?: it.borderDirection
    }

    override fun merge(other: UIBorder?) {
        TODO("Not yet implemented")
    }
}