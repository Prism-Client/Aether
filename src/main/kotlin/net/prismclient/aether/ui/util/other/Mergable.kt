package net.prismclient.aether.ui.util.other

/**
 * [Mergable] merges this, [T] and another instance of [T] into a new instance of [T]. This overwrites
 * any null values of the provided value.
 *
 * @author sen
 * @since 1.0
 */
@Suppress("SpellCheckingInspection")
interface Mergable<T> {
    /**
     * Applies the values of this to any null values of [other] in a new instance of this.
     */
    fun merge(other: T?): T
}