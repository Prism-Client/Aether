package net.prismclient.aether.ui.composition.util

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.core.util.shorthands.ifNotNull
import net.prismclient.aether.core.util.shorthands.lerp
import net.prismclient.aether.core.util.shorthands.or
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.Radius
import net.prismclient.aether.core.util.property.UIProperty

/**
 * [UIBackground] is a property for a component's Modifier. It represents the background of a [Composable].
 *
 * @author sen
 * @since 1.0
 */
open class UIBackground : ComposableShape<Composable>(), UIProperty<UIBackground> {
    override var width: UIUnit<*>? = 1.crel
    override var height: UIUnit<*>? = 1.crel

    // TODO: Border

    var backgroundColor: UIColor? = null
    var backgroundRadius: Radius? = null

    override fun compose(composable: Composable?) {
        super.compose(composable)
        initialX = composable!!.relX
        initialY = composable.relY
        backgroundRadius?.compose(composable)
    }

    override fun render() {
        renderer {
            color(backgroundColor)
            rect(initialX + x.dp, initialY + y.dp, width.dp, height.dp, backgroundRadius)
        }
    }

    override fun UIUnit<*>?.compute(composable: Composable, yaxis: Boolean) {
        // Update based on the relative values instead of the normal
        this?.compute(composable, composable.relWidth, composable.relHeight, yaxis)
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.backgroundColor = backgroundColor?.copy()
        it.backgroundRadius = backgroundRadius?.copy()
    }

    override fun merge(other: UIBackground?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
            width = other.width or width
            height = other.height or height
            backgroundColor = other.backgroundColor or backgroundColor
            backgroundRadius = other.backgroundRadius or backgroundRadius
        }
    }

    override fun animate(start: UIBackground?, end: UIBackground?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(x, start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(y, start?.y, end?.y, fraction)
        }
        ifNotNull(start?.width, end?.width) {
            width = width ?: 0.px
            width!!.lerp(width, start?.width, end?.width, fraction)
        }
        ifNotNull(start?.height, end?.height) {
            height = height ?: 0.px
            height!!.lerp(height, start?.height, end?.height, fraction)
        }
        ifNotNull(start?.backgroundColor, end?.backgroundColor) {
            backgroundColor = backgroundColor ?: UIColor(0)
            backgroundColor!!.animate(start?.backgroundColor, end?.backgroundColor, fraction)
        }
        ifNotNull(start?.backgroundRadius, end?.backgroundRadius) {
            backgroundRadius = backgroundRadius ?: Radius()
            backgroundRadius!!.animate(start?.backgroundRadius, end?.backgroundRadius, fraction)
        }
    }

    override fun toString(): String = "UIBackground(x=$x, y=$y, width=$width, height=$height, backgroundColor=$backgroundColor, backgroundRadius=$backgroundRadius)"
}

fun UIBackground.color(color: UIColor) = apply {
    backgroundColor = color
}

fun UIBackground.radius(radius: Radius) = apply {
    backgroundRadius = radius
}