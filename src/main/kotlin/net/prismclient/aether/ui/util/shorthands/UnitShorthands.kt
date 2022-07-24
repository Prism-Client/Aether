package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.unit.type.UIPixel
import net.prismclient.aether.ui.unit.type.UIRelativePixel

/**
 * Creates a pixel unit with the given [value].
 */
fun px(value: Number) = UIPixel(value.toFloat())

/**
 * Creates a relative value; a value which scales based on the parent width/height times the given [value].
 */
fun rel(value: Number) = UIRelativePixel(value.toFloat())