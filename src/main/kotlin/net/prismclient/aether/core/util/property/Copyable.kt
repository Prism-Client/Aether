package net.prismclient.aether.core.util.property

/**
 * [Copyable] is used for creating deep copies (same properties, different references) of an object.
 *
 * @author sen
 * @since 1.0
 * @see UIProperty
 */
interface Copyable<T> {
    /**
     * Every property within this, [T], is copied to a new object of this.
     */
    fun copy(): T
}