package net.prismclient.aether.ui.registry

import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.style.Style
import net.prismclient.aether.ui.util.shorthands.Block
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * [UIRegistry] stores global and local states for Modifiers and Styles. The default values
 * of Modifiers and Styles are stored here, and applied to them.
 *
 * @author sen
 * @since 1.0
 */
object UIRegistry {
    val globalModifiers: HashMap<String, Modifier> = hashMapOf()
    val globalStyles: HashMap<String, Style<*>> = hashMapOf()

    inline fun <reified T : Modifier> registerModifier(clazz: T) {
        globalModifiers[clazz::class.simpleName!!] = clazz
    }

    fun test() {
        val helloWorld = object {
            val hello = "Hello"
            val world = "World"
            // object expressions extend Any, so `override` is required on `toString()`
            override fun toString() = "$hello $world"
        }
    }
}