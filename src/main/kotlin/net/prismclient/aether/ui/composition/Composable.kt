package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.alignment.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.shorthands.px

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

    open fun updatePosition() {
        x?.compute(this, false)
        y?.compute(this, false)
    }

    open fun updateSize() {
        width?.compute(this, false)
        height?.compute(this, false)
    }

    /**
     * Invoked when this is to be composed. Units and other properties should be initialized at this point.
     */
    abstract fun compose()

    /**
     * Invoked when this should be rendered.
     */
    abstract fun render()

    // -- Util -- //

    operator fun UIUnit?.unaryPlus(): Float = this?.cachedValue ?: 0f

    // -- Shorthands -- //
    open fun constrain(x: Number, y: Number, width: Number, height: Number) =
            constrain(px(x), px(y), px(width), px(height))

    open fun constrain(x: UIUnit, y: UIUnit, width: UIUnit, height: UIUnit) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
    }

    open fun size(width: Number, height: Number) = size(px(width), px(height))

    open fun size(width: UIUnit, height: UIUnit) {
        this.width = width
        this.height = height
    }
}