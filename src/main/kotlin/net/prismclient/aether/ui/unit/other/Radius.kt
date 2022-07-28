package net.prismclient.aether.ui.unit.other

import net.prismclient.aether.ui.util.other.Copyable

/**
 *
 *
 * @author sen
 * @since 1.0
 */
class Radius(
    var topLeft: Float = 0f,
    var topRight: Float = 0f,
    var bottomRight: Float = 0f,
    var bottomLeft: Float = 0f
) : Copyable<Radius> {
    override fun copy(): Radius = Radius(topLeft, topRight, bottomRight, bottomLeft)

    override fun toString(): String =
        "UIRadius(topLeft=$topLeft, topRight=$topRight, bottomRight=$bottomRight, bottomLeft=$bottomLeft)"
}