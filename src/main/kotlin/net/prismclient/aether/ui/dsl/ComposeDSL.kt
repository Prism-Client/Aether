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
 * Used to easily compose [Composable]s. To start composing create a [Compose] block.
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
     * Creates a new [Composition] with the given [name] and [block].
     */
    inline fun Composition(
        name: String,
        modifier: CompositionModifier<*> = DefaultCompositionModifier(),
        block: Block<Composition>,
    ): Composition = Aether.instance.createComposition(name, modifier).also {
        val previous = activeComposition
        activeComposition = it
        block(it)
        activeComposition = previous
    }


    /**
     * Accepts the given [Composable], [T] and applies the active state to it while applying the given [block].
     */
    inline fun <T : Composable> composable(composable: T, block: Block<T>): T {
        check()
        composable.composition = activeComposition ?: Aether.instance.defaultComposition!!
        composable.parent = activeComposable ?: activeComposition ?: Aether.instance.defaultComposition

        if (composable.parent is ComposableGroup) (composable.parent as ComposableGroup).children.add(composable)

        val previous = activeComposable

        activeComposable = composable
        block(composable)
        activeComposable = previous

        return composable
    }
}