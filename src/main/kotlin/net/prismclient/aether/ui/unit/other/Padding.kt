package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.*
import net.prismclient.aether.ui.util.shorthands.*
import net.prismclient.aether.ui.util.shorthands.ifNotNull
import net.prismclient.aether.ui.util.shorthands.lerp

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
) : Property<Padding> {
    override fun update(composable: Composable?) {
        composable!!
        top?.compute(composable, composable.width.dp, composable.height.dp, true)
        right?.compute(composable, composable.width.dp, composable.height.dp, false)
        bottom?.compute(composable, composable.width.dp, composable.height.dp, true)
        left?.compute(composable, composable.width.dp, composable.height.dp, false)
    }

    override fun copy(): Padding = Padding(top?.copy(), right?.copy(), bottom?.copy(), left?.copy())

    override fun merge(other: Padding?): Padding {
        if (other == null) return copy()
        return Padding(
            top = other.top or top,
            right = other.right or right,
            bottom = other.bottom or bottom,
            left = other.left or left
        )
    }

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