package net.prismclient.aether.ui.composition.util

import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.property.UIProperty
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.Renderer
import net.prismclient.aether.ui.shape.ComposableShape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.UIRadius

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
    var backgroundRadius: UIRadius? = null
    var backgroundBorder: UIBorder? = null

    override fun compose(composable: Composable?) {
        super.compose(composable)
        initialX = composable!!.relX
        initialY = composable.relY
        backgroundRadius?.compose(composable)
        backgroundBorder?.compose(this)
    }

    override fun render() {
        Renderer {
            color(backgroundColor)
            rect(initialX + x.dp, initialY + y.dp, width.dp, height.dp, backgroundRadius)
        }
        backgroundBorder?.render()
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
        it.backgroundColor = backgroundColor.copy
        it.backgroundRadius = backgroundRadius.copy
        it.backgroundBorder = backgroundBorder.copy
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
            backgroundRadius = backgroundRadius ?: UIRadius()
            backgroundRadius!!.animate(start?.backgroundRadius, end?.backgroundRadius, fraction)
        }
    }

    override fun toString(): String =
        "UIBackground(x=$x, y=$y, width=$width, height=$height, backgroundColor=$backgroundColor, backgroundRadius=$backgroundRadius)"
}

fun UIBackground.color(color: UIColor) = apply {
    backgroundColor = color
}

fun UIBackground.radius(radius: UIRadius) = apply {
    backgroundRadius = radius
}