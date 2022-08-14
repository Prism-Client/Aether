package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.core.util.other.ComposableGroup

/**
 * [UILayout] is a composable used for controlling a group of components in a specific way. For example,
 *
 * @author sen
 * @since 1.0
 *
 * @param overrideChildren When true, the children's property overridden is enabled during the compose stage.
 */
abstract class UILayout(modifier: UIModifier<*>, protected val overrideChildren: Boolean) : Composable(modifier),
    ComposableGroup {
    override val children: ArrayList<Composable> = arrayListOf()

    /**
     * The size of the layout which is set after [updateLayout] is invoked.
     */
    open lateinit var layoutSize: Size

    override fun compose() {
        modifier.preCompose(this)
        composeSize()
        composePosition()
        // Invoke the updateUnits function after
        // calculating the relevant properties of this.
        updateUnits()

        // Update the parent and override (if necessary) to the children
        children.forEach {
            it.parent = this
            if (overrideChildren) it.overridden = true
        }
        // Calculate the initial and possible the final layout
        layoutSize = updateLayout()

        if (dynamic) {
            // Update the units after calculating the potential size and
            // re-update the layout as updateUnits probably changed something
            updateUnits()
            updateLayout()
        }

        modifier.compose(this)
    }

    /**
     * Invoked after the general properties of this have been already set, such as the
     * size and position. At this point, any dynamic units should be calculated. If the
     * unit is a dynamic unit, the width and height of the layout might need to be calculated
     * if this is the case, this function will be invoked twice: once before the initial layout
     * calculation, and one right after.
     *
     * @see layoutSize
     */
    abstract fun updateUnits()

    /**
     * Invoked when the layout needs an update. There are two ways this is naturally invoked:
     *
     * **Initial:** This is ensured to happen.
     *
     * **Dynamic:** This will happen if a dynamic unit is set within the properties of this
     *
     * Because the property is dynamic, if the layout changes the components within it will not
     * lay properly. To combat this, the layout is updated twice if deemed necessary.
     *
     * @return Expects the size of the layout with the origin point as the (x, y) of this.
     */
    abstract fun updateLayout(): Size

    override fun render() {
        modifier.preRender()
        children.forEach(Composable::render)
        modifier.render()
    }

    // -- Utility -- //

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
}