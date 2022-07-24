package net.prismclient.aether.ui.composition.component

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.util.other.UICloneable


/**
 * @author sen
 * @since 1.0
 */
abstract class UIComponent<T> : Composable(), UICloneable<T> {
    override fun compose() {
        updatePosition()
        updateSize()
    }

    override fun render() {
        renderComponent()
    }

    abstract fun renderComponent()
}