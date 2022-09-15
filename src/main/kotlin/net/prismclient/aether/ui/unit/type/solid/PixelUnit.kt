package net.prismclient.aether.ui.unit.type.solid

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit

/**
 * The simplest of all [UIUnit]s which represents the given [value] in pixels. It is a static unit,
 * and does not require the parent of the composition to re-update if necessary.
 *
 * @author sen
 * @since 1.0
 */
open class PixelUnit(value: Float) : UIUnit<PixelUnit>(value) {
    // Since this is a static unit, and the value of it is
    // known at  the creation, return the value directly.
    @Suppress("SuspiciousVarProperty")
    override var cachedValue: Float = value
        get() = value

    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float = value

    override fun copy(): PixelUnit = PixelUnit(value)

    override fun toString(): String = "Pixel($value, $cachedValue)"
}