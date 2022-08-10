package net.prismclient.aether.ui.util.other

import net.prismclient.aether.ui.composition.Composable

/**
 * Indicates a class which contains a variable for a list of Composable(s), named [children].
 *
 * @author sen
 * @since 1.0
 */
interface ComposableGroup {
    val children: ArrayList<Composable>
}