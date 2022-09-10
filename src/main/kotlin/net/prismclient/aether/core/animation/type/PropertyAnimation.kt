package net.prismclient.aether.core.animation.type

import net.prismclient.aether.core.animation.Animation
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier

/**
 * Only re-rasterize the composition after the state changes. Useful if the color
 * or other properties which do not affect the layout are set.
 *
 * @authors en
 * @since 1.0
 * @see LayoutAnimation
 */
class PropertyAnimation<C : Composable, M : UIModifier<M>> : Animation<C, M>() {
    override fun update() {
        super.update()
        composable.composition.compose()
    }
}