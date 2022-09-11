package net.prismclient.aether.core.util.property

/**
 * Runs [block] given that [value] or [value1] is not null. Returns the value of [block] or false. This
 * is used for checking if the active or next keyframe of an animation. is not null and returning if the
 * composition needs to be recomposed
 */
internal inline fun checkIf(value: Any?, value1: Any? = null, block: () -> Boolean): Boolean =
    if (value != null || value1 != null) block() else false