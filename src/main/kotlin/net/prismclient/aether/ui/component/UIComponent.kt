package net.prismclient.aether.ui.component

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.util.other.Copyable


/**
 * @author sen
 * @since 1.0
 */
abstract class UIComponent<T>(val modifier: Modifier) : Composable(),
    Copyable<T> {
    override fun compose() {
        modifier.preUpdate(this)
        updatePosition()
        updateSize()
        modifier.update(this)
    }

    override fun render() {
        modifier.preRender()
        renderComponent()
        modifier.render()
    }

    abstract fun renderComponent()
}