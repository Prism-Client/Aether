package net.prismclient.aether.core.util.property

import net.prismclient.aether.ui.composition.Composable


/**
 * [Focusable] is an interface to inform Aether that this is a focusable [Composable]. A focusable
 * composable is a composable eligible for events which target specifically focused composables, such
 * as a scrolling or keyPress event. The intended purpose is to ensure that multiple layouts, for
 * example, are not being scrolled at the same time, or multiple text fields being typed at once.
 *
 * Implementing this makes it immediately eligible for focusability.
 *
 * @author sen
 * @since 1.0
 */
interface Focusable {
    /**
     * If Aether determines that this is a valid [Composable] for focus, it will first ask
     * this function if it wants to be focused.
     */
    fun wantsFocus(): Boolean
}