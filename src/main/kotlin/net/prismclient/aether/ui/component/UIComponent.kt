package net.prismclient.aether.ui.component

import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.ui.composer.ComposableContext
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier

/**
 * [UIComponent] is the core of all components. With that said, all components must extend this.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIComponent<T : UIComponent<T>>(modifier: UIModifier<*>) : Composable(modifier), Copyable<T> {
    override var parent: Composable? = null

    override fun compose(context: ComposableContext) {
        modifier.preCompose(context)
        composeSize(context)
        composePosition(context)
        update(context)
        modifier.compose(context)
    }

    /**
     * Invoked after the components bounds have been updated.
     */
    open fun update(context: ComposableContext) {}

    override fun render() {
        modifier.preRender()
        renderComponent()
        modifier.render()
    }

    abstract fun renderComponent()
}