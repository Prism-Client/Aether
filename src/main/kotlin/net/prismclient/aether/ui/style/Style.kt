package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.util.other.Animatable
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.other.Mergable

/**
 * Most components require more information than the default properties provided by Modifier. Modifier
 * only provides more positioning/plotting information, so [Style] takes care of the rest. An example is
 * for a label, where the font size, and font color need to be changed.
 *
 * @author sen
 * @since 1.0
 */
abstract class Style<T> : Copyable<T>, Mergable<T>, Animatable<T>