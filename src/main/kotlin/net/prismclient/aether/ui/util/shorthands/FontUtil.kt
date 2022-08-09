package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.core.Aether
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.UIRenderer

/**
 * [Font] provides utilities for obtaining metrics for fonts.
 *
 * @author sen
 * @since 1.0
 */
private class Font

/**
 * Returns an array of the metrics from the most recent text render call. The returned value
 * is a FloatArray formatted as:
 *
 *      minX - The minimum x coordinate of the text.
 *      minY - The minimum y coordinate of the text.
 *      maxX - The maximum x coordinate of the text.
 *      maxY - The maximum y coordinate of the text.
 *      advance - The x coordinate of the next character's glyph position
 *
 * @see [UIRenderer.fontBounds]
 */
fun fontBounds(): FloatArray = Aether.renderer.fontBounds()

/**
 * Returns an array of the metrics from the given String at the coordinate
 * point (0, 0). See [fontBounds] for the returned array format.
 */
inline val String.bounds: FloatArray get() = Aether.renderer.fontBounds(this)

/**
 * Returns the minimum x coordinate of the given text array.
 *
 * @see fontBounds
 */
inline val FloatArray.minX: Float get() = this[0]

/**
 * Returns the minimum y coordinate of the given text array.
 *
 * @see fontBounds
 */
inline val FloatArray.minY: Float get() = this[1]

/**
 * Returns the maximum x coordinate of the given text array.
 *
 * @see fontBounds
 */
inline val FloatArray.maxX: Float get() = this[2]

/**
 * Returns the maximum y coordinate of the given text array.
 *
 * @see fontBounds
 */
inline val FloatArray.maxY: Float get() = this[3]

/**
 * Returns the x coordinate of the next character's glyph position.
 *
 * @see fontBounds
 */
inline val FloatArray.advance: Float get() = this[4]

/**
 * Returns the width of the most recent text render call.
 *
 * @see fontBounds
 */
fun fontWidth(): Float = fontBounds().maxX - fontBounds().minX

/**
 * Returns the height of the most recent text render call.
 *
 * @see fontBounds
 */
fun fontHeight(): Float = fontBounds().maxY - fontBounds().minY

/**
 * Returns the ascender of the most recent text render call.
 */
fun fontAscender(): Float = UIRendererDSL.renderer.fontAscender()

/**
 * Returns the descender of the most recent text render call.
 */
fun fontDescender(): Float = UIRendererDSL.renderer.fontDescender()