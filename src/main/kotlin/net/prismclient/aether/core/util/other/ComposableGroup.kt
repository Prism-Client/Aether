package net.prismclient.aether.core.util.other

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.ComposeDSL

/**
 * Indicates to Aether, specifically [ComposeDSL] that this contains a group of [Composable]s, [children].
 *
 * @author sen
 * @since 1.0
 */
interface ComposableGroup {
    val children: ArrayList<Composable>
}