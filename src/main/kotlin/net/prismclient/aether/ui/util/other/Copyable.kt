package net.prismclient.aether.ui.util.other

/**
 * [Copyable] is used for creating deep copies (same properties, different references) of an object.
 *
 * @author sen
 * @since 1.0
 * @see Property
 */
interface Copyable<T> {
    /**
     * Every property within this, [T], is copied to a new object of this.
     */
    fun copy(): T
}