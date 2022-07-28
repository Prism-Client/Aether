package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.UIComposition

typealias Block<T> = T.() -> Unit

/**
 * Creates a new composition with the given [name], and applies the [block]. The composition is automatically
 * closed after the block is executed, so [Aether.activeComposition] will be null once the block is completed.
 */
inline fun compose(name: String, block: Block<UIComposition>): UIComposition {
    val composition = Aether.instance.createComposition(name)
    Aether.instance.activeComposition = composition
    composition.block()
    Aether.instance.activeComposition = null
    return composition
}

fun FloatArray.minX(): Float = this[0]
fun FloatArray.minY(): Float = this[1]
fun FloatArray.maxX(): Float = this[2]
fun FloatArray.maxY(): Float = this[3]

fun FloatArray.x(): Float = this[0]
fun FloatArray.y(): Float = this[1]
fun FloatArray.width(): Float = this[2]
fun FloatArray.height(): Float = this[3]

fun FloatArray.advance(): Float = this[4]