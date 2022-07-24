package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.util.other.UICloneable

/**
 * @author sen
 * @since 1.0
 */
class UIRadius(
    var topLeft: Float = 0f,
    var topRight: Float = 0f,
    var bottomRight: Float = 0f,
    var bottomLeft: Float = 0f
) : UICloneable<UIRadius> {
    override fun clone(): UIRadius = UIRadius(topLeft, topRight, bottomRight, bottomLeft)

    override fun toString(): String =
        "UIRadius(topLeft=$topLeft, topRight=$topRight, bottomRight=$bottomRight, bottomLeft=$bottomLeft)"
}