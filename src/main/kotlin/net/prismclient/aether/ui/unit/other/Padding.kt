package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.Animatable
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.shorthands.ifNotNull
import net.prismclient.aether.ui.util.shorthands.lerp
import net.prismclient.aether.ui.util.shorthands.px

/**
 * Padding is a property of [Modifier] which represents the bounds of a component. When padding is added, the content size
 * does not change but the bounds of the actual component will change. This will affect properties such as the background
 * that rely on the "relative" bounds instead of the inner bounds.
 *
 * @author sen
 * @since 1.0
 */
open class Padding(
    var top: UIUnit<*>?,
    var right: UIUnit<*>?,
    var bottom: UIUnit<*>?,
    var left: UIUnit<*>?
) : Copyable<Padding>, Animatable<Padding> {
    fun update(composable: Composable) {
        top?.compute(composable, true)
        right?.compute(composable, false)
        bottom?.compute(composable, true)
        left?.compute(composable, false)
    }

    override fun copy(): Padding = Padding(top?.copy(), right?.copy(), bottom?.copy(), left?.copy())

    override fun animate(start: Padding?, end: Padding?, fraction: Float) {
        ifNotNull(start?.top, end?.top) {
            top = top ?: 0.px
            top!!.lerp(start?.top, end?.top, fraction)
        }
        ifNotNull(start?.right, end?.right) {
            right = right ?: 0.px
            right!!.lerp(start?.right, end?.right, fraction)
        }
        ifNotNull(start?.bottom, end?.bottom) {
            bottom = bottom ?: 0.px
            bottom!!.lerp(start?.bottom, end?.bottom, fraction)
        }
        ifNotNull(start?.left, end?.left) {
            left = left ?: 0.px
            left!!.lerp(start?.left, end?.left, fraction)
        }
    }

    override fun toString(): String = "Padding(top=$top, right=$right, bottom=$bottom, left=$left)"
}