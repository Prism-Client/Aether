package net.prismclient.aether.ui.composer.hint

import net.prismclient.aether.ui.composer.ComposerHint
import net.prismclient.aether.ui.composition.Composable

/**
 * A hint which indicates that this depends on the given [composable] and that it needs to be composed prior to this.
 *
 * @author sen
 * @since 1.0
 */
class DependentHint(val composable: Composable) : ComposerHint()

/**
 * A hint which indicates that this depends on the give [composables] and that they need to be composed prior to this.
 *
 * @author sen
 * @since 1.0
 */
class DependentGroupHint(val composables: Array<Composable>) : ComposerHint()