package net.prismclient.aether.ui.registry

import net.prismclient.aether.core.util.shorthands.copy
import net.prismclient.aether.ui.modifier.UIModifier
import net.prismclient.aether.ui.registry.UIRegistry.disableApplyingModifiers
import net.prismclient.aether.ui.registry.UIRegistry.enableApplyingModifiers
import net.prismclient.aether.ui.registry.UIRegistry.registerModifier
import net.prismclient.aether.ui.registry.UIRegistry.registerStyle
import net.prismclient.aether.ui.style.Style

/**
 * [UIRegistry] stores global Modifiers and Styles which are automatically applied at creation. To register
 * a Modifier or Style pass a style, or modifier to [registerModifier], or [registerStyle] respectively.
 *
 * Sometimes, you might not want to automatically apply a Modifier or Style, so using [enableApplyingModifiers]
 * and [disableApplyingModifiers] will disable it for it's respected type.
 *
 * @author sen
 * @since 1.0
 */
object UIRegistry {
    private val modifiers: HashMap<String, UIModifier<*>> = hashMapOf()
    private val styles: HashMap<String, Style<*, *>> = hashMapOf()
    var applyModifiers: Boolean = true
        private set
    var applyStyles: Boolean = true
        private set

    /**
     * Registers a modifier with the given [name] to the modifier list.
     *
     * @see register
     */
    fun registerModifier(name: String, modifier: UIModifier<*>) {
        modifiers[name] = modifier
    }

    /**
     * Registers a style with the given [name] to the style list.
     *
     * @see register
     */
    fun registerStyle(name: String, modifier: Style<*, *>) {
        styles[name] = modifier
    }

    /**
     * Returns a [UIModifier], or null from the given [name].
     *
     * @see obtainStyle
     * @see enableApplyingModifiers
     * @see disableApplyingModifiers
     */
    fun obtainModifier(name: String): UIModifier<*>? = if (applyModifiers) modifiers[name] else null

    /**
     * Returns a [Style], or null from the given [name].
     *
     * @see obtainModifier
     * @see enableApplyingStyles
     * @see disableApplyingStyles
     */
    fun obtainStyle(name: String): Style<*, *>? = if (applyStyles) styles[name] else null

    fun enableApplyingModifiers() {
        applyModifiers = true
    }

    fun enableApplyingStyles() {
        applyStyles = true
    }

    fun disableApplyingModifiers() {
        applyModifiers = false
    }

    fun disableApplyingStyles() {
        applyStyles = false
    }
}

/**
 * Registers the given modifier with the given [name] to [UIRegistry]. When set to the active modifier,
 * all properties of it are applied to the active modifier.
 */
fun register(modifier: UIModifier<*>, name: String = modifier::class.simpleName!!) {
    UIRegistry.registerModifier(name, modifier)
}

/**
 * Creates a copy of the [style] and registers it with the given [name] to [UIRegistry]. When set
 * to the active modifier, all properties of it are applied to the active modifier.
 */
fun register(style: Style<*, *>, name: String = style::class.simpleName!!) {
    UIRegistry.registerStyle(name, style.copy())
}