package net.prismclient.aether.core.util.property

/**
 * Represents a class which takes two instances of the same class and expects
 * the properties of the classes to be lerped (linear interpolation) based on
 * a given fraction. For example, if the class contains a singular integer, and
 * the first class is 0.2, while the second is 0.8. If the fraction is 0.5, then
 * the value of this, would be 0.5.
 *
 * @author sen
 * @since 1.0
 * @see Property
 */
@Suppress("SpellCheckingInspection")
interface Animatable<T> {
    /**
     * Expects each individual property of this, to be lerped based on the given fraction
     * and the [start] and [end] point classes. (start - end) * progress + start. If either
     * values are null, the expected value should be this.
     */
    fun animate(start: T?, end: T?, fraction: Float)
}