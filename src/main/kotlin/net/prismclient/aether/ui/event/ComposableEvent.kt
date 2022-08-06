package net.prismclient.aether.ui.event

import net.prismclient.aether.ui.composition.Composable

/**
 * [ComposableEvent] accounts for the majority of events handled through Aether. Any events with this
 * as it's superclass will propagate up the composition tree to the top and request a re-draw of the
 * composition.
 *
 * @author sen
 * @since 1.0
 */
abstract class ComposableEvent(var composable: Composable) : UIEvent