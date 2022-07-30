package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.unit.UIUnit
import kotlin.math.roundToInt

// Internal shorthands for Aether to reduce the amount of boilerplate code.

internal typealias Block<T> = T.() -> Unit

// -- Null Checking -- //

internal fun Any?.isNull() = this == null

internal fun Any?.notNull() = this != null

/**
 * Runs [block] if either [value] or [value1] are not null.
 */
internal inline fun ifNotNull(value: Any?, value1: Any? = null, block: () -> Unit) {
    if (value != null || value1 != null) block()
}

// -- Lerping -- //

/**
 * Linearly interpolates between [start] and [end] by [progress] which are Ints, and returns an Int.
 */
internal fun lerp(start: Int, end: Int, progress: Float): Int = start + ((end - start) * progress).toInt()

/**
 * Linearly interpolates between [start] and [end] by [progress] which are Floats, and returns a Float.
 */
internal fun lerp(start: Float, end: Float, progress: Float) = start + (end - start) * progress.roundToInt()

/**
 * Linearly interpolates between [start] and [end] by [progress] which are Units, and adjust the cached value to the output.
 */
internal fun UIUnit<*>.lerp(start: UIUnit<*>?, end: UIUnit<*>?, progress: Float) {
    this.cachedValue = start.dp + (end.dp - start.dp) * progress
}