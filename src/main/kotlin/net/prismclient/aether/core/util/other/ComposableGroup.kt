package net.prismclient.aether.core.util.other

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.composition.Composable

/**
 * Indicates to Aether that this is a class which stores a group of composables, known as [children].
 *
 * @author sen
 * @since 1.0
 */
interface ComposableGroup {
    val children: ArrayList<Composable>
}