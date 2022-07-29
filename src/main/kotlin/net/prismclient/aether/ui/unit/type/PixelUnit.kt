package net.prismclient.aether.ui.unit.type

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
    override fun updateCache(composable: Composable?, yaxis: Boolean): Float = value

    override fun copy(): PixelUnit = PixelUnit(value)

    override fun toString(): String = "Pixel($value)"
}