package net.prismclient.aether.core.event

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.input.UIKey
import net.prismclient.aether.ui.composition.Composable

/**
 * A type of event which indicates that the only composable invoked is the focused component, [Aether.focusedComponent].
 */
abstract class FocusedEvent(val focusedComposable: Composable) : CustomEvent(focusedComposable)

/**
 * A [FocusedEvent] which invokes the focused composable. [dstX] and [dstY] represents the distance
 * the mouse or trackpad was scrolled in the given axis.
 *
 * @author sen
 * @since 1.0
 */
class MouseScrolled(val dstX: Float, val dstY: Float, focusedComposable: Composable) : FocusedEvent(focusedComposable)

/**
 * A [FocusedEvent] which invokes the focused composable. If the key cannot be mapped to a character,
 * [character] will equal to '\u0000'.
 *
 * @author sen
 * @since 1.0
 */
class KeyPressed(val character: Char, val key: UIKey, focusedComposable: Composable) : FocusedEvent(focusedComposable)