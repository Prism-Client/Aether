package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.core.util.property.UIProperty
import net.prismclient.aether.core.util.property.UIUniqueProperty
import net.prismclient.aether.ui.composition.Composable

/**
 * Some components require more information than the default properties provided by Modifier. Modifier
 * only provides more positioning/plotting based information, so [Style] takes care of the rest. Styles
 * target a specific property group, such as a font. Take the label, where the font size, and font color
 * need to be changed.
 *
 * This is the superclass for all Styles. It accepts two generic properties, [S], which is this, and [T]
 * which, the expected [Composable] type.
 *
 * @author sen
 * @since 1.0
 */
abstract class Style<S : Style<S, T>, T : Composable> : UIUniqueProperty<S, T> {
    init {
        applyStyle(this::class.simpleName!!)
    }

    open fun preCompose() {}

    /**
     * Merges the given style from [UIRegistry] into this, if not null.
     */
    @Suppress("unchecked_cast")
    open fun applyStyle(name: String): S {
        val activeStyle = UIRegistry.obtainStyle(name)
        if (activeStyle != null) merge(activeStyle as S)
        return this as S
    }
}