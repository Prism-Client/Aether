package net.prismclient.aether.ui.shape.component.background

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.renderer.UIColor
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.unit.other.Radius
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.Animatable
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.shorthands.*
import net.prismclient.aether.ui.util.shorthands.ifNotNull

/**
 * [UIBackground] is a property for a component's Modifier. It represents the background of a [Composable].
 *
 * @author sen
 * @since 1.0
 */
open class UIBackground : ComposableShape(), Copyable<UIBackground>, Animatable<UIBackground> {
    override var width: UIUnit<*>? = rel(1)
    override var height: UIUnit<*>? = rel(1)

    // TODO: Border

    var backgroundColor: UIColor? = null
    var backgroundRadius: Radius? = null

    override fun render() {
        renderer {
            color(backgroundColor)
            rect(x.dp, y.dp, width.dp, height.dp)
        }
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.x = x
        it.y = y
        it.width = width
        it.height = height
        it.backgroundColor = backgroundColor
        it.backgroundRadius = backgroundRadius
    }

    override fun animate(start: UIBackground?, end: UIBackground?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(start?.y, end?.y, fraction)
        }
        ifNotNull(start?.width, end?.width) {
            width = width ?: 0.px
            width!!.lerp(start?.width, end?.width, fraction)
        }
        ifNotNull(start?.height, end?.height) {
            height = height ?: 0.px
            height!!.lerp(start?.height, end?.height, fraction)
        }
    }
}

fun UIBackground.backgroundColor(color: UIColor) = apply {
    backgroundColor = color
}

fun UIBackground.backgroundRadius(radius: Radius) = apply {
    backgroundRadius = radius
}