package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit

/**
 * A [UIUnit] which scales based on the width or height of the composable's parent. If
 * there is no parent, the screen's width and height are which this scales on.
 *
 * @author sen
 * @since 1.0
 */
open class RelativeUnit(value: Float) : UIUnit<RelativeUnit>(value) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        TODO("Not yet implemented")
    }

    override fun copy(): RelativeUnit = RelativeUnit(value)

    override fun toString(): String = "Relative($value)"
}