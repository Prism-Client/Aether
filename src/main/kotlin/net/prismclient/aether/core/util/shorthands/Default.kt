package net.prismclient.aether.core.util.shorthands

import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.UIRadius

// Provides extensions for creating defaults of various classes if the given is null.

inline val UIUnit<*>?.default: UIUnit<*>? get() = this ?: 0.px

inline val UIColor?.default: UIColor get() = this ?: UIColor(0)

inline val UIRadius?.default: UIRadius get() = this ?: UIRadius()