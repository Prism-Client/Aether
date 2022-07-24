package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.alignment.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit

/**
 * [Composable] is an object which Aether uses to compose the UI. In other words, it is a class
 * which has position and size properties and is played out within a composition.
 *
 * @author sen
 * @since 1.0
 */
abstract class Composable {
    var parent: Composable? = null

    /**
     * Returns the width of [parent], or the width of the display.
     */
    val parentWidth: Float
        get() = if (parent != null) parent!!.parentWidth else Aether.instance.displayWidth

    /**
     * Returns the height of [parent], or the height of the display.
     */
    val parentHeight: Float
        get() = if (parent != null) parent!!.parentHeight else Aether.instance.displayHeight

    /**
     * Returns true if this has been composed at least once.
     */
    var composed: Boolean = false

    /**
     * Returns true if this or a child (but not sub-children) have a relative/dynamic unit.
     */
    var dynamic: Boolean = false

    var x: UIUnit? = null
    var y: UIUnit? = null
    var width: UIUnit? = null
    var height: UIUnit? = null
    var anchorPoint: UIAnchorPoint? = null

    /**
     * Invoked when this is to be composed. Units and other properties should be initialized at this point.
     */
    abstract fun compose()

    /**
     * Invoked when this should be rendered.
     */
    abstract fun render()

    operator fun UIUnit?.unaryPlus(): Float = this?.value ?: 0f
}