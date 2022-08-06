package net.prismclient.aether.ui.modifier

import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.composition.util.color
import net.prismclient.aether.ui.composition.util.radius
import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.ui.renderer.UIColor
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding
import net.prismclient.aether.ui.unit.other.Radius
import net.prismclient.aether.ui.unit.type.dynamic.SizeUnit
import net.prismclient.aether.ui.util.other.Animatable
import net.prismclient.aether.ui.util.other.Copyable
import net.prismclient.aether.ui.util.other.Mergable
import net.prismclient.aether.ui.alignment.UIAlignment.*
import net.prismclient.aether.ui.util.shorthands.*

/**
 * [UIModifier] contains information such as the position, background, padding and other properties
 * that reflect the component that this is passed to. [UIModifier] is an inheritable class where custom
 * properties and effects can be added if the existing API is not enough. If Modifier is inherited, the
 * functions [UIModifier.copy], [UIModifier.merge] and [UIModifier.animate] should
 * be overriden to avoid unwanted behavior.
 *
 * @author sen
 * @since 1.0
 */
@Suppress("unchecked_cast", "LeakingThis")
abstract class UIModifier<T : UIModifier<T>> : Copyable<T>, Mergable<T>, Animatable<T> {
    open var x: UIUnit<*>? = null
    open var y: UIUnit<*>? = null
    open var width: UIUnit<*>? = null
    open var height: UIUnit<*>? = null
    open var anchorPoint: AnchorPoint? = null

    open var padding: Padding? = null
    open var margin: Margin? = null

    open var background: UIBackground? = null

    init {
        applyModifier(this::class.simpleName!!)
    }

    /**
     * Invoked prior to updating general properties of the composable, such as the position and size.
     */
    open fun preUpdate(component: Composable) {}

    open fun update(component: Composable) {
        background?.update(component)
    }

    /**
     * Merges the given modifier from [UIRegistry] into this, if not null.
     */
    open fun applyModifier(name: String): T {
        val activeModifier = UIRegistry.obtainModifier(name)
        // Apply the properties of the active modifier if not null
        if (activeModifier != null) merge(activeModifier as T)
        return this as T
    }

    /**
     * Invoked prior to the component draw.
     */
    open fun preRender() {
        background?.render()
    }

    /**
     * Invoked after the component draw
     */
    open fun render() {
    }

    /**
     * Adjusts the x of this to the given [value].
     */
    fun x(value: UIUnit<*>): T {
        x = value
        return this as T
    }

    /**
     * Adjusts the y of this to the given [value].
     */
    fun y(value: UIUnit<*>): T {
        y = value
        return this as T
    }

    /**
     * Adjusts the width of this to the given [value].
     */
    fun width(value: UIUnit<*>): T {
        width = value
        return this as T
    }

    /**
     * Adjusts the height of this to the given [value].
     */
    fun height(value: UIUnit<*>) : T {
        height = value
        return this as T
    }

    /**
     * Adjusts the position of this Modifier<*> to the given [x] and [y] coordinate units.
     */
    fun position(x: UIUnit<*>, y: UIUnit<*>): T {
        this.x = x
        this.y = y
        return this as T
    }

    /**
     * Adjusts the position of this Modifier<*> to the given [x] and [y] coordinate values.
     */
    fun position(x: Number, y: Number) = position(x.px, y.px)

    /**
     * Adjusts the size of this Modifier<*> to the given [width] and [height] units.
     */
    fun size(width: UIUnit<*>, height: UIUnit<*>): T {
        this.width = width
        this.height = height
        return this as T
    }

    /**
     * Adjusts the size of this Modifier<*> to the given [width] and [height] values.
     */
    fun size(width: Number, height: Number) = size(width.px, height.px)

    /**
     * Constrains this Modifier<*> to be within the bounds of the given units.
     */
    fun <T : UIModifier<*>> T.constrain(x: UIUnit<*>, y: UIUnit<*>, width: UIUnit<*>, height: UIUnit<*>): T {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        return this as T
    }

    /**
     * Constrains this Modifier<*> to be within the bounds of the given values.
     */
    fun constrain(x: Number, y: Number, width: Number, height: Number) =
        constrain(x.px, y.px, width.px, height.px)

    /**
     * Sets the anchor point to the given [alignment].
     *
     * @see SizeUnit
     */
    fun anchor(alignment: UIAlignment): T {
        anchorPoint = anchorPoint ?: AnchorPoint()
        anchorPoint!!.align(alignment)
        return this as T
    }

    /**
     * Sets the anchor point to the given [x], and [y].
     *
     * @see SizeUnit
     */
    fun anchor(x: UIUnit<*>, y: UIUnit<*>): T {
        anchorPoint = anchorPoint ?: AnchorPoint()
        anchorPoint!!.x = x
        anchorPoint!!.y = y
        return this as T
    }

    /**
     * Aligns and positions this Modifier<*> to the given [alignment] relative to its parent.
     */
    fun control(alignment: UIAlignment): T {
        anchor(alignment)
        x = when (alignment) {
            TOPCENTER, CENTER, BOTTOMCENTER -> 0.5
            TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> 1.0
            else -> 0f
        }.rel
        y = when (alignment) {
            MIDDLELEFT, CENTER, MIDDLERIGHT -> 0.5
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> 1.0
            else -> 0f
        }.rel
        return this as T
    }

    /**
     * Sets the padding of this Modifier<*> to the given units.
     */
    fun padding(top: UIUnit<*>?, right: UIUnit<*>?, bottom: UIUnit<*>?, left: UIUnit<*>?): T {
        padding = Padding(top, right, bottom, left)
        return this as T
    }

    /**
     * Sets the padding of this Modifier<*> to the given values.
     */
    fun padding(top: Number, right: Number, bottom: Number, left: Number) =
        padding(top.px, right.px, bottom.px, left.px)

    /**
     * Sets the margin of this Modifier<*> to the given units.
     */
    fun margin(top: UIUnit<*>?, right: UIUnit<*>?, bottom: UIUnit<*>?, left: UIUnit<*>?): T {
        margin = Margin(top, right, bottom, left)
        return this as T
    }

    /**
     * Sets the margin of this Modifier<*> to the given values.
     */
    fun margin(top: Number, right: Number, bottom: Number, left: Number) =
        margin(top.px, right.px, bottom.px, left.px)

    /**
     * Sets the background color of this [Modifier] to the given [color]. A background is allocated if none is set.
     */
    fun backgroundColor(color: UIColor): T {
        background = background ?: UIBackground()
        background!!.color(color)
        return this as T
    }

    /**
     * Sets the background radius of this [Modifier] to the given [radius]. A background is allocated if none is set.
     */
    fun backgroundRadius(radius: Radius): T {
        background = background ?: UIBackground()
        background!!.radius(radius)
        return this as T
    }
}

/**
 * Returns a new instance of the default implementation of [UIModifier],
 */
@Suppress("Nothing_to_Inline", "FunctionName")
inline fun Modifier(): DefaultModifier = DefaultModifier()

/**
 * The default implementation of [UIModifier]. The copy, merge and animate functions are
 * overridden to supply for the default properties of [UIModifier].
 * 
 * @since 1.0
 * @author sen
 */
open class DefaultModifier : UIModifier<DefaultModifier>() {
    override fun copy(): DefaultModifier = DefaultModifier().also {
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
        it.anchorPoint = anchorPoint?.copy()
        it.padding = padding?.copy()
        it.margin = margin?.copy()
        it.background = background?.copy()
    }

    override fun merge(other: DefaultModifier?) {
        if (other != null) {
            x = other.x or x
            y = other.y or y
            width = other.width or width
            height = other.height or height
            anchorPoint = other.anchorPoint or anchorPoint
            padding = other.padding or padding
            margin = other.margin or margin
            background = other.background or background
        }
    }

    override fun animate(start: DefaultModifier?, end: DefaultModifier?, fraction: Float) {
        ifNotNull(start?.x, end?.x) {
            x = x ?: 0.px
            x!!.lerp(x, start?.x, end?.x, fraction)
        }
        ifNotNull(start?.y, end?.y) {
            y = y ?: 0.px
            y!!.lerp(y, start?.y, end?.y, fraction)
        }
        ifNotNull(start?.width, end?.width) {
            width = width ?: 0.px
            width!!.lerp(width, start?.width, end?.width, fraction)
        }
        ifNotNull(start?.height, end?.height) {
            height = height ?: 0.px
            height!!.lerp(height, start?.height, end?.height, fraction)
        }
        ifNotNull(start?.anchorPoint, end?.anchorPoint) {
            anchorPoint = anchorPoint ?: AnchorPoint()
            anchorPoint!!.animate(start?.anchorPoint, end?.anchorPoint, fraction)
        }
        ifNotNull(start?.padding, end?.padding) {
            padding = padding ?: Padding(null, null, null, null)
            padding!!.animate(start?.padding, end?.padding, fraction)
        }
        ifNotNull(start?.margin, end?.margin) {
            margin = margin ?: Margin(null, null, null, null)
            margin!!.animate(start?.margin, end?.margin, fraction)
        }
    }
}