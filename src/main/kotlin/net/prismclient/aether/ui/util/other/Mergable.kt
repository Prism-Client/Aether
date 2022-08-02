package net.prismclient.aether.ui.util.other

/**
 * [Mergable] merges this, [T] and another instance of [T] into a new instance of [T]. This overwrites
 * any null values of the provided value. The new instance's properties should be a deep copy.
 *
 * @author sen
 * @since 1.0
 * @see Property
 */
@Suppress("SpellCheckingInspection")
interface Mergable<T> {
    /**
     * Expects a new instance of this, [T], which has the properties of [other], and fills
     * the null properties with this.
     */
    fun merge(other: T?): T
}