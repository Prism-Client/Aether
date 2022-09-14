package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.core.animation.AnimationContext
import net.prismclient.aether.core.util.property.UIProperty
import net.prismclient.aether.core.util.shorthands.or
import net.prismclient.aether.ui.composer.ComposableContext
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.compute

/**
 * Padding is a property of [UIModifier] which represents the bounds of a component. When padding is added, the content size
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
) : UIProperty<Padding> {
    override fun compose(context: ComposableContext) {
        context!!
        top?.compute(context, true)
        right?.compute(context, false)
        bottom?.compute(context, true)
        left?.compute(context, false)
    }

    override fun animate(context: AnimationContext<*>, start: Padding?, end: Padding?, progress: Float) {
        TODO("Not yet implemented")
    }

    override fun copy(): Padding = Padding(top?.copy(), right?.copy(), bottom?.copy(), left?.copy())

    override fun merge(other: Padding?) {
        if (other != null) {
            top = other.top or top
            right = other.right or right
            bottom = other.bottom or bottom
            left = other.left or left
        }
    }

    operator fun component1() = top

    operator fun component2() = right

    operator fun component3() = bottom

    operator fun component4() = left

    override fun toString(): String = "Padding(top=$top, right=$right, bottom=$bottom, left=$left)"
}