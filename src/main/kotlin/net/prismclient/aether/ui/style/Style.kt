package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.font.UIFont
import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.core.util.property.Property

/**
 * Most components require more information than the default properties provided by Modifier. Modifier
 * only provides more positioning/plotting information, so [Style] takes care of the rest. An example is
 * for a label, where the font size, and font color need to be changed.
 *
 * This is the superclass for all Styles. It accepts a generic property, [T] which is the type of this. All
 * [Property] functions are expected to be inherited.
 *
 * @author sen
 * @since 1.0
 */
abstract class Style<T> : Property<T> {
    init {
        applyStyle(this::class.simpleName!!)
    }

    open fun preCompose() {}

    /**
     * Merges the given style from [UIRegistry] into this, if not null.
     */
    @Suppress("unchecked_cast", "LeakingThis")
    open fun applyStyle(name: String): T {
        val activeStyle = UIRegistry.obtainStyle(name)
        if (activeStyle != null) merge(activeStyle as T)
        return this as T
    }
}