package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit

/**
 * [Shape] is the superclass for all shapes, which are not a [Composable] that are rendered on screen.
 *
 * @author sen
 * @since 1.0
 */
abstract class Shape {
    open var x: UIUnit<*>? = null
    open var y: UIUnit<*>? = null
    open var width: UIUnit<*>? = null
    open var height: UIUnit<*>? = null

    abstract fun render()
}