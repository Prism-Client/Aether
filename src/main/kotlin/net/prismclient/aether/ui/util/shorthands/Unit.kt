package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.unit.other.Radius
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.OperationUnit
import net.prismclient.aether.ui.unit.type.PixelUnit
import net.prismclient.aether.ui.unit.type.RelativeUnit

/**
 * Creates a pixel unit with the given [value].
 */
fun px(value: Number) = PixelUnit(value.toFloat())

/**
 * Creates a relative value; a value which scales based on the parent width/height times the given [value].
 */
fun rel(value: Number) = RelativeUnit(value.toFloat())

/**
 * Creates a [PixelUnit] of the given value.
 */
inline val Number.px: PixelUnit get() = PixelUnit(this.toFloat())

/**
 * Creates a [RelativeUnit] of the given value.
 */
inline val Number.rel: RelativeUnit get() = RelativeUnit(this.toFloat())

/**
 * Creates a [Radius] with the given value for each corner.
 */
inline val Number.radius: Radius get() = Radius(this.toFloat())

/**
 * Creates a [Radius] with the given pixel value
 */
inline val PixelUnit.radii: Radius get() = Radius(this.value)

operator fun UIUnit<*>.plus(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.ADD)

operator fun UIUnit<*>.minus(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.SUBTRACT)

operator fun UIUnit<*>.times(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.MULTIPLY)

operator fun UIUnit<*>.div(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.DIVIDE)

operator fun Number.plus(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.ADD)

operator fun Number.minus(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.SUBTRACT)

operator fun Number.times(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.MULTIPLY)

operator fun Number.div(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.DIVIDE)

/**
 * Returns the cached value of the given unit, or 0f if the unit is null.
 */
inline val UIUnit<*>?.dp: Float get() = this?.cachedValue ?: 0f