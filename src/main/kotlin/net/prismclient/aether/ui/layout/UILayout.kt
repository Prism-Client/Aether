package net.prismclient.aether.ui.layout

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.util.other.ComposableGroup

/**
 * [UILayout] is a composable used for controlling a group of components in a specific way. For example,
 *
 * @author sen
 * @since 1.0
 *
 * @param override When true, the children's property overridden is enabled during the compose stage.
 */
abstract class UILayout(modifier: UIModifier<*>, protected val override: Boolean) : Composable(modifier), ComposableGroup {
    override val children: ArrayList<Composable> = arrayListOf()

    override fun compose() {
        // hasDynamic property?


        modifier.preUpdate(this)
        updatePosition()
        updateSize()
        children.forEach {
            it.parent = this
            if (override) it.overridden = true
        }
        updateLayout()
        modifier.update(this)
    }

    override fun render() {
        modifier.preRender()
        children.forEach(Composable::render)
        modifier.render()
    }

    /**
     * Adds the given [component] to this layout.
     *
     * @return True if the component was not already added.
     */
    open fun addChild(component: UIComponent<*>) {
        component.parent = this
        children.add(component)
    }

    /**
     * Removes the given [component] from this. The parent of the child is set to null if it is this.
     *
     * @return True if the component was removed.
     */
    open fun removeChild(component: UIComponent<*>) {
        if (component.parent == this)
            component.parent = null
        children.remove(component)
    }

    /**
     * Invoked when the layout needs to be composed.
     */
    abstract fun updateLayout()

    // TODO: Utility methods for controlling composables
    // -- Utility -- //

}