package net.prismclient.aether.core.util.property

/**
 * [Property] provides a shorthand for implementing the 4 key single function interfaces
 * for copying, merging, animating, and updating. Shapes and properties, such as the padding
 * or radius require these interfaces to provide this necessary features to make Aether work.
 *
 * @author sen
 * @since 1.0
 */
interface Property<T> : Updatable, Copyable<T>, Mergable<T>, Animatable<T>