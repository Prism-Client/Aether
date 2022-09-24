package net.prismclient.aether.ui.layout

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.ui.composition.Composable

/**
 * @author sen
 * @since 1.0
 */
class CustomLayout @JvmOverloads constructor(
    name: String = "CustomLayout",
    modifier: LayoutModifier<*> = LayoutModifier(),
    layoutStyle: BoxLayoutStyle = BoxLayoutStyle(),

    /**
     * Invoked when the potential size of the layout is calculated.
     */
    val sizeCalculation: CustomLayout.() -> Size? = { Size(0f, 0f) },

    /**
     * Invoked after the potential size is calculated. This is for any units
     * that need to be calculated prior to calculating the layout.
     */
    val unitCalculation: CustomLayout.(layoutSize: Size?) -> Unit = {},

    /**
     * Invoked when the layout needs to be calculated. With the given data, the children
     * should be laid how they need to be. Expects the final layout size to be returned.
     */
    val layout: CustomLayout.(children: ArrayList<Composable>, layoutSize: Size?) -> Size,
) : BoxLayout(name, modifier, layoutStyle) {

    override fun updateUnits() = unitCalculation.invoke(this, potentialSize)

    override fun updateLayout(): Size = layout.invoke(this, children, potentialSize)
}