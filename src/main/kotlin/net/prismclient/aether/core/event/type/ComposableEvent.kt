package net.prismclient.aether.core.event.type

import net.prismclient.aether.core.event.UIEvent
import net.prismclient.aether.ui.composition.Composable

/**
 * [ComposableEvent] accounts for the majority of events handled through Aether.
 *
 * @author sen
 * @since 1.0
 */
abstract class ComposableEvent(val composable: Composable) : UIEvent