package net.prismclient.aether.ui.component

import net.prismclient.aether.core.animation.Animatable
import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier
import java.lang.reflect.Modifier

/**
 * [UIComponent] is the core of all components. With that said, all components must extend this.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIComponent<T : UIComponent<T>>(modifier: UIModifier<*>) : Composable(modifier), Copyable<T> {
    override var parent: Composable? = null

    override fun compose() {
        modifier.preCompose(this)
        composeSize()
        composePosition()
        update()
        modifier.compose(this)
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