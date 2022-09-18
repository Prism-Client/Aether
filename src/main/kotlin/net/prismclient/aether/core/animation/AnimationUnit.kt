package net.prismclient.aether.core.animation

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit

/**
 * [AnimationUnit] acts identical to a [PixelUnit] however, it is used to indicate an animation
 * and the modifier's position or size properties will be set to this during the animation and
 * restored after to the necessary unit.
 */
class AnimationUnit(val actualUnit: UIUnit<*>?, value: Float) : UIUnit<AnimationUnit>(value) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float = value

    override fun copy(): AnimationUnit = AnimationUnit(actualUnit, value)

    override fun toString(): String = "AnimationUnit($actualUnit, $value)"
}