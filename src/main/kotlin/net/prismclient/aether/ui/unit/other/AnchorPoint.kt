package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.Animatable
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.shorthands.ifNotNull
import net.prismclient.aether.ui.util.shorthands.lerp
import net.prismclient.aether.ui.util.shorthands.px

/**
 * Expects a composition which is to be offset (subtracted) by the given units. Relative
 * values scale based on the size of the composable.
 *
 * @author sen
 * @since 1.0
 */
class AnchorPoint(var x: UIUnit<*>?, var y: UIUnit<*>?) : Copyable<AnchorPoint>, Animatable<AnchorPoint> {
    fun update(composable: Composable) {
        x?.compute(composable, false)
        y?.compute(composable, true)
    }

    override fun copy(): AnchorPoint = AnchorPoint(x?.copy(), y?.copy())

    override fun animate(start: AnchorPoint?, end: AnchorPoint?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(start?.y, end?.y, fraction)
        }
    }
}