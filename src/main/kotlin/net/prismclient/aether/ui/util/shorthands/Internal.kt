package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.other.Mergable
import kotlin.math.roundToInt

// Internal shorthands for Aether to reduce the amount of boilerplate code.

internal typealias Block<T> = T.() -> Unit

// -- Null Checking -- //

internal fun Any?.isNull() = this == null

internal fun Any?.notNull() = this != null

/**
 * Returns a deep copy of the receiver if not null, else other. If other is null, return null.
 *
 * This is used for [Mergable] to return the other value else this.
 */
@Suppress("unchecked_cast")
internal infix fun <T : Copyable<*>> T?.or(other: T?): T? = (this?.copy() ?: other) as T?

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
 * Linearly interpolates between [start] and [end] by [progress] which are Units, and adjust the
 * cached value to the output. If [start] or [end] are null, the value of [initial] is used. The
 * value of [initial] is considered 0f if null.
 */
internal fun UIUnit<*>.lerp(initial: UIUnit<*>?, start: UIUnit<*>?, end: UIUnit<*>?, progress: Float) {
    this.cachedValue = (start?.dp ?: initial.dp) + ((end?.dp ?: initial.dp) - (start?.dp ?: initial.dp)) * progress
}

// -- Other -- //
@Suppress("unchecked_cast")
internal inline val <T : Copyable<*>> T?.copy: T get() = this?.copy() as T