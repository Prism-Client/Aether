package net.prismclient.aether.core.util.property

import net.prismclient.aether.ui.composition.Composable

/**
 * [UIProperty] provides a shorthand for implementing the 4 key single function interfaces
 * for copying, merging, animating, and updating. Shapes and properties, such as the padding
 * or radius require these interfaces to provide this necessary features to make Aether work.
 *
 * @author sen
 * @since 1.0
 * @see UIUniqueProperty
 */
interface UIProperty<T> : Updatable<Composable>, Copyable<T>, Mergable<T>, Animatable<T>

/**
 * A [UIProperty], where the second generic type, [C] represents the [Composable] for [Updatable]
 *
 * @author sen
 * @since 1.0
 * @see UIProperty
 */
interface UIUniqueProperty<T, C : Composable> : Updatable<C>, Copyable<T>, Mergable<T>, Animatable<T>