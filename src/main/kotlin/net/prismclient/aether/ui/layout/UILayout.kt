package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.ifNotNull
import net.prismclient.aether.core.util.shorthands.lerp
import net.prismclient.aether.core.util.shorthands.or
import net.prismclient.aether.core.util.shorthands.px
import net.prismclient.aether.ui.modifier.DefaultModifier
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding

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

/**
 * [LayoutModifier] the [UIModifier] type required for [UILayout]s. It is an abstract class, and holds the
 * properties for deciding what to do with overflowing content within layouts. [T] is expected to be the class
 * which extends this.
 *
 * @author sen
 * @since 1.0
 * @see DefaultLayoutModifier
 */
abstract class LayoutModifier<T : LayoutModifier<T>> : UIModifier<T>() {

}

class DefaultLayoutModifier : LayoutModifier<DefaultLayoutModifier>() {
    override fun copy(): DefaultLayoutModifier = DefaultLayoutModifier().also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.anchorPoint = anchorPoint?.copy()
        it.padding = padding?.copy()
        it.margin = margin?.copy()
        it.background = background?.copy()
        TODO("Copy not yet implemented.")
    }

    override fun merge(other: DefaultLayoutModifier?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
            width = other.width or width
            height = other.height or height
            anchorPoint = other.anchorPoint or anchorPoint
            padding = other.padding or padding
            margin = other.margin or margin
            background = other.background or background
        }
        TODO("Merge not yet implemented.")
    }

    override fun animate(start: DefaultLayoutModifier?, end: DefaultLayoutModifier?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(x, start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(y, start?.y, end?.y, fraction)
        }
        ifNotNull(start?.width, end?.width) {
            width = width ?: 0.px
            width!!.lerp(width, start?.width, end?.width, fraction)
        }
        ifNotNull(start?.height, end?.height) {
            height = height ?: 0.px
            height!!.lerp(height, start?.height, end?.height, fraction)
        }
        ifNotNull(start?.anchorPoint, end?.anchorPoint) {
            anchorPoint = anchorPoint ?: AnchorPoint()
            anchorPoint!!.animate(start?.anchorPoint, end?.anchorPoint, fraction)
        }
        ifNotNull(start?.padding, end?.padding) {
            padding = padding ?: Padding(null, null, null, null)
            padding!!.animate(start?.padding, end?.padding, fraction)
        }
        ifNotNull(start?.margin, end?.margin) {
            margin = margin ?: Margin(null, null, null, null)
            margin!!.animate(start?.margin, end?.margin, fraction)
        }
        TODO("Animate not yet implemented.")
    }

}