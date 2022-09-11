package net.prismclient.aether.core.util.shorthands

import net.prismclient.aether.core.color.UIColor

// Provides extensions for creating defaults of various classes if the given is null.

inline val UIColor?.default: UIColor get() = this ?: UIColor(0)