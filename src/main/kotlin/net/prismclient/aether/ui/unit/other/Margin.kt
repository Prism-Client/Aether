package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.unit.UIUnit

/**
 * The margin is the space between each component within a layout. This will be offset by [top] and
 * [right], and the next component will be offset by the [right] and [bottom]. If the component is
 * not within a restrictive layout, the [top] and [right] will offset by the given position, and [bottom]
 * and [left] will be ignored.
 *
 * @author sen
 * @since 1.0
 */
open class Margin(top: UIUnit<*>?, right: UIUnit<*>?, bottom: UIUnit<*>?, left: UIUnit<*>?) : Padding(top, right, bottom, left) {
    override fun copy(): Margin = Margin(top?.copy(), right?.copy(), bottom?.copy(), left?.copy())
}