package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.composer.ComposableContext
import net.prismclient.aether.ui.unit.UIUnit

/**
 * A [UIUnit] which scales based on the width or height of the composable's parent. If
 * there is no parent, the screen's width and height are which this scales on. Because
 * the parent size is known immediately, this is not a relative unit.
 *
 * @author sen
 * @since 1.0
 */
open class RelativeUnit(value: Float) : UIUnit<RelativeUnit>(value) {
    override fun updateCache(context: ComposableContext?, width: Float, height: Float, yaxis: Boolean): Float {
        return if (!yaxis) {
            context?.composable?.parentWidth() ?: Aether.instance.displayWidth
        } else {
            context?.composable?.parentHeight() ?: Aether.instance.displayHeight
        } * value
    }

    override fun copy(): RelativeUnit = RelativeUnit(value)

    override fun toString(): String = "Relative($value, $cachedValue)"
}