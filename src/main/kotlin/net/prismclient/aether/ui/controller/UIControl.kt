package net.prismclient.aether.ui.controller

import net.prismclient.aether.core.util.other.ComposableGroup
import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.composition.Composable
import java.util.function.Consumer
import kotlin.reflect.KClass

class UIControl<T : Composable>(
    var selectedComposable: T,
    private val type: KClass<T>
) : ComposableGroup {
    override val children: ArrayList<Composable> = arrayListOf()

    private var selectionAction: Consumer<T>? = null
    private var deselectionAction: Consumer<T>? = null

    init {
        children.add(selectedComposable)
    }

    fun select(composable: T) {
        selectedComposable = composable
        selectionAction?.accept(composable)
        children.filter { type.isInstance(it) }.forEach {
            if (it != composable) deselectionAction?.accept(it as T)
        }
    }

    fun onSelect(action: Consumer<T>) {
        selectionAction = action
    }

    fun onDeselect(action: Consumer<T>) {
        deselectionAction = action
    }
}

inline fun <reified T : Composable> Control(selectedComposable: T, block: Block<UIControl<T>>) =
    UIControl(selectedComposable, T::class).also(block)