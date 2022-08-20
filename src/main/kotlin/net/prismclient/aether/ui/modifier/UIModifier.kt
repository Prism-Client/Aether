package net.prismclient.aether.ui.modifier

import net.prismclient.aether.core.color.UIAlpha
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.composition.util.color
import net.prismclient.aether.ui.composition.util.radius
import net.prismclient.aether.ui.registry.UIRegistry
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.core.util.shorthands.ifNotNull
import net.prismclient.aether.core.util.shorthands.lerp
import net.prismclient.aether.core.util.shorthands.or
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.AnchorPoint
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding
import net.prismclient.aether.ui.unit.other.Radius
import net.prismclient.aether.ui.unit.type.dynamic.SizeUnit
import net.prismclient.aether.core.util.property.Animatable
import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.core.util.property.Mergable
import net.prismclient.aether.ui.alignment.UIAlignment.*

/**
 * [UIModifier] contains information such as the position, background, padding and other properties that
 * reflect the component that this is passed to. [UIModifier] is an inheritable class where custom
 * properties and effects can be added if the existing API is not enough. If Modifier is inherited, the
 * functions [UIModifier.copy], [UIModifier.merge] and [UIModifier.animate] should be overriden to avoid
 * unwanted behavior. The second generic type, [C] represents the a [Composable] or a subclass of it which
 * can be expected from the functions of this.
 *
 * @author sen
 * @since 1.0
 * @see DefaultModifier
 */
@Suppress("Unchecked_Cast", "LeakingThis")
abstract class UIModifier<M : UIModifier<M>> : Copyable<M>, Mergable<M>, Animatable<M> {
    open var x: UIUnit<*>? = null
    open var y: UIUnit<*>? = null
    open var width: UIUnit<*>? = null
    open var height: UIUnit<*>? = null
    open var anchorPoint: AnchorPoint? = null

    open var padding: Padding? = null
    open var margin: Margin? = null
    open var opacity: UIAlpha? = null // TODO: Add support for interfaces and actual implementation

    open var background: UIBackground? = null

    init {
        applyModifier(this::class.simpleName!!)
    }

    /**
     * Invoked prior to updating general properties of the composable, such as the position and size.
     */
    open fun preCompose(component: Composable) {}

    open fun compose(composable: Composable) {
        background?.compose(composable)
    }

    /**
     * Merges the given modifier from [UIRegistry] into this, if not null.
     */
    open fun applyModifier(name: String): M {
        val activeModifier = UIRegistry.obtainModifier(name)
        // Apply the properties of the active modifier if not null
        if (activeModifier != null) merge(activeModifier as M)
        return this as M
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
    fun x(value: UIUnit<*>): M {
        x = value
        return this as M
    }

    /**
     * Adjusts the y of this to the given [value].
     */
    fun y(value: UIUnit<*>): M {
        y = value
        return this as M
    }

    /**
     * Adjusts the width of this to the given [value].
     */
    fun width(value: UIUnit<*>): M {
        width = value
        return this as M
    }

    /**
     * Adjusts the height of this to the given [value].
     */
    fun height(value: UIUnit<*>) : M {
        height = value
        return this as M
    }

    /**
     * Adjusts the position of this Modifier<*> to the given [x] and [y] coordinate units.
     */
    fun position(x: UIUnit<*>, y: UIUnit<*>): M {
        this.x = x
        this.y = y
        return this as M
    }

    /**
     * Adjusts the position of this Modifier<*> to the given [x] and [y] coordinate values.
     */
    fun position(x: Number, y: Number) = position(x.px, y.px)

    /**
     * Adjusts the size of this Modifier<*> to the given [width] and [height] units.
     */
    fun size(width: UIUnit<*>, height: UIUnit<*>): M {
        this.width = width
        this.height = height
        return this as M
    }

    /**
     * Adjusts the size of this Modifier<*> to the given [width] and [height] values.
     */
    fun size(width: Number, height: Number) = size(width.px, height.px)

    /**
     * Constrains this Modifier<*> to be within the bounds of the given units.
     */
    fun constrain(x: UIUnit<*>, y: UIUnit<*>, width: UIUnit<*>, height: UIUnit<*>): M {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        return this as M
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
    fun anchor(alignment: UIAlignment): M {
        anchorPoint = anchorPoint ?: AnchorPoint()
        anchorPoint!!.align(alignment)
        return this as M
    }

    /**
     * Sets the anchor point to the given [x], and [y].
     *
     * @see SizeUnit
     */
    fun anchor(x: UIUnit<*>, y: UIUnit<*>): M {
        anchorPoint = anchorPoint ?: AnchorPoint()
        anchorPoint!!.x = x
        anchorPoint!!.y = y
        return this as M
    }

    /**
     * Aligns and positions this Modifier<*> to the given [alignment] relative to its parent.
     */
    fun control(alignment: UIAlignment): M {
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
        return this as M
    }

    /**
     * Sets the padding of this Modifier<*> to the given units.
     */
    fun padding(top: UIUnit<*>?, right: UIUnit<*>?, bottom: UIUnit<*>?, left: UIUnit<*>?): M {
        padding = Padding(top, right, bottom, left)
        return this as M
    }

    /**
     * Sets the padding of this Modifier<*> to the given values.
     */
    fun padding(top: Number, right: Number, bottom: Number, left: Number) =
        padding(top.px, right.px, bottom.px, left.px)

    /**
     * Sets the margin of this Modifier<*> to the given units.
     */
    fun margin(top: UIUnit<*>?, right: UIUnit<*>?, bottom: UIUnit<*>?, left: UIUnit<*>?): M {
        margin = Margin(top, right, bottom, left)
        return this as M
    }

    /**
     * Sets the margin of this Modifier<*> to the given values.
     */
    fun margin(top: Number, right: Number, bottom: Number, left: Number) =
        margin(top.px, right.px, bottom.px, left.px)

    /**
     * Sets the background color of this [Modifier] to the given [color]. A background is allocated if none is set.
     */
    fun backgroundColor(color: UIColor): M {
        background = background ?: UIBackground()
        background!!.color(color)
        return this as M
    }

    /**
     * Sets the background radius of this [Modifier] to the given [radius]. A background is allocated if none is set.
     */
    fun backgroundRadius(radius: Radius): M {
        background = background ?: UIBackground()
        background!!.radius(radius)
        return this as M
    }
}

/**
 * Returns a new instance of the default implementation of [UIModifier] : [DefaultModifier].
 */
@Suppress("FunctionName")
fun Modifier(): DefaultModifier = DefaultModifier()

/**
 * The default implementation of [UIModifier]. No unique properties are added, but the copy, merge and animate functions
 * are overridden to supply the required functions to [UIModifier].
 * 
 * @since 1.0
 * @author sen
 * @see UIModifier
 * @see Modifier
 */
class DefaultModifier : UIModifier<DefaultModifier>() {
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

        // TODO: update composable properties
    }
}