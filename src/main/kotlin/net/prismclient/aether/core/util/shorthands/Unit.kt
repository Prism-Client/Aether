package net.prismclient.aether.core.util.shorthands

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.Margin
import net.prismclient.aether.ui.unit.other.Padding
import net.prismclient.aether.ui.unit.other.UIRadius
import net.prismclient.aether.ui.unit.type.dynamic.RelativeUnit
import net.prismclient.aether.ui.unit.type.dynamic.SizeUnit
import net.prismclient.aether.ui.unit.type.other.OperationUnit
import net.prismclient.aether.ui.unit.type.range.RangeUnit
import net.prismclient.aether.ui.unit.type.solid.PixelUnit

/**
 * UnitKt provides utility functions for creating and reading units.
 *
 * @author sen
 * @since 1.0
 */

// -- Kotlin Functions -- //

/**
 * Creates a [PixelUnit] of the given value. A [PixelUnit] is a 1:1 unit of pixels.
 *
 * @see PixelUnit
 */
@get:JvmName("px")
inline val Number.px: PixelUnit get() = PixelUnit(this.toFloat())

/**
 * Creates a [RelativeUnit] of the given value. A relative unit scales based on the width/height of the
 * composable's parent, or the width / height of the screen. If composable or it's parent is null.
 *
 * @see RelativeUnit
 */
@get:JvmName("rel")
inline val Number.rel: RelativeUnit get() = RelativeUnit(this.toFloat())

/**
 * Creates a [SizeUnit] of the given value. A composable relative unit scales based on
 * the width / height of the composable which is passed through on update.
 *
 * @see SizeUnit
 */
@get:JvmName("crel")
inline val Number.crel: SizeUnit get() = SizeUnit(this.toFloat())

/**
 * Creates a [UIRadius] with the given value for each corner.
 */
@get:JvmName("radius")
inline val Number.radius: UIRadius get() = px.radii

/**
 * Creates a [UIRadius] with the given unit. The unit is copied for each corner except the topLeft, where
 * the original UIUnit reference is placed.
 */
@get:JvmName("radii")
inline val UIUnit<*>.radii: UIRadius get() = UIRadius(this, this.copy(), this.copy(), this.copy())

// -- Util -- //

/**
 * Returns the cached value of the given unit, or 0f if the unit is null.
 */
@get:JvmName("dp")
inline val UIUnit<*>?.dp: Float get() = this?.cachedValue ?: 0f

// -- Operator Functions -- //

operator fun UIUnit<*>?.plus(other: UIUnit<*>?): OperationUnit = OperationUnit(this, other, OperationUnit.Operation.ADD)

operator fun UIUnit<*>?.minus(other: UIUnit<*>?): OperationUnit =
    OperationUnit(this, other, OperationUnit.Operation.SUBTRACT)

operator fun UIUnit<*>?.times(other: UIUnit<*>?): OperationUnit =
    OperationUnit(this, other, OperationUnit.Operation.MULTIPLY)

operator fun UIUnit<*>?.div(other: UIUnit<*>?): OperationUnit =
    OperationUnit(this, other, OperationUnit.Operation.DIVIDE)

operator fun Number.plus(other: UIUnit<*>?): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.ADD)

operator fun Number.minus(other: UIUnit<*>?): OperationUnit =
    OperationUnit(this.px, other, OperationUnit.Operation.SUBTRACT)

operator fun Number.times(other: UIUnit<*>?): OperationUnit =
    OperationUnit(this.px, other, OperationUnit.Operation.MULTIPLY)

operator fun Number.div(other: UIUnit<*>?): OperationUnit =
    OperationUnit(this.px, other, OperationUnit.Operation.DIVIDE)

// -- Range Operator Functions -- //

/**
 * Creates a [RangeUnit] and ensures that this is at least the minimum value. If this is a range unit,
 *  the minimum value is set to [min] and this is returned.
 */
fun UIUnit<*>.atLeast(min: UIUnit<*>): RangeUnit {
    if (this is RangeUnit) {
        this.min = min
        return this
    }
    return RangeUnit(this, min, null)
}

/**
 * Creates a [RangeUnit] and ensures that this is at most the maximum value. If this is a range unit,
 *  the maximum value is set to [max] and this is returned.
 */
fun UIUnit<*>.atMost(max: UIUnit<*>): RangeUnit {
    if (this is RangeUnit) {
        this.max = max
        return this
    }
    return RangeUnit(this, null, max)
}

/**
 * Creates a [RangeUnit] and ensures that this is between the minimum and maximum values. If this is a
 * [RangeUnit], the minimum and maximum values are set to [min] and [max] and this is returned.
 */
fun UIUnit<*>.range(min: UIUnit<*>? = null, max: UIUnit<*>? = null): RangeUnit {
    if (this is RangeUnit) {
        this.min = min
        this.max = max
        return this
    }
    return RangeUnit(this, min, max)
}

// -- Padding and Margin Functions -- //

/**
 * Creates a [Padding] with the given value for each side in pixels.
 */
@get:JvmName("padding")
inline val Number.padding: Padding get() = Padding(this.px, this.px, this.px, this.px)

/**
 * Creates a [Margin] with the given value for each side in pixels.
 */
@get:JvmName("margin")
inline val Number.margin: Margin get() = Margin(this.px, this.px, this.px, this.px)