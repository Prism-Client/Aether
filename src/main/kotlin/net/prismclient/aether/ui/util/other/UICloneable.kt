package net.prismclient.aether.ui.util.other

/**
 * Returns a deep copy of the given object, [T].
 *
 * @author sen
 * @since 1.0
 */
interface UICloneable<T> {
    /**
     * Returns a deep copy of everything within [T].
     */
    fun clone(): T
}