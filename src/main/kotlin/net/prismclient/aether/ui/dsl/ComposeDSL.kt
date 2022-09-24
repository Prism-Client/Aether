package net.prismclient.aether.ui.dsl

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.Composition
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.DefaultCompositionModifier
import net.prismclient.aether.ui.dsl.ComposeDSL.begin
import net.prismclient.aether.ui.dsl.ComposeDSL.end
import java.lang.IllegalStateException

/**
 * Executes the given [block] within the [ComposeDSL] scope.
 */
inline fun Compose(block: Block<ComposeDSL>) {
    begin()
    ComposeDSL.block()
    end()
}

/**
 *
 * @author sen
 * @since 1.0
 *
 * @see composable
 */
object ComposeDSL {
    var activeComposition: Composition? = null
    var activeComposable: Composable? = null


    @JvmStatic
    fun begin() {}


    @JvmStatic
    fun end() {
        activeComposition = null
        activeComposable = null
    }

    @JvmStatic
    fun check() {
        if (Aether.instance.activeScreen == null)
            throw IllegalStateException("There is no active screen?")
        if (Aether.instance.defaultComposition == null)
            throw IllegalStateException("There is no default composition. How did you even get here?")
    }

    /**
     *
     */
    inline fun <T : Composable> composable(composable: T, block: Block<T>): T = composable.apply {
        check()

        val previousComposition = activeComposition
        val previousComposable = activeComposable

        composition = if (this is Composition) {
            activeComposition = this
            previousComposition ?: this
        } else {
            activeComposition ?: Aether.instance.defaultComposition!!
        }

        parent = activeComposable

        if (parent is ComposableGroup) (parent as ComposableGroup).children.add(this)

        activeComposable = this

        block()

        activeComposable = previousComposable
        activeComposition = previousComposition

    }
}