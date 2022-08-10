package net.prismclient.aether.ui.component

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.util.other.Copyable


/**
 * [UIComponent] is the core of all components. With that said, all components must extend this.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIComponent<T>(modifier: UIModifier<*>) : Composable(modifier), Copyable<T> {
    override var parent: Composable? = null

    override fun compose() {
        modifier.preUpdate(this)
        updatePosition()
        updateSize()
        update()
        modifier.update(this)
    }

    /**
     * Invoked after the components bounds have been updated.
     */
    open fun update() {}

    override fun render() {
        modifier.preRender()
        renderComponent()
        modifier.render()
    }

    abstract fun renderComponent()
}