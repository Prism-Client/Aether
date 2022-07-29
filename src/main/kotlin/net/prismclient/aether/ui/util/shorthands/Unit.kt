package net.prismclient.aether.ui.util.shorthands

import net.prismclient.aether.ui.unit.other.Radius
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.ComposableRelativeUnit
import net.prismclient.aether.ui.unit.type.OperationUnit
import net.prismclient.aether.ui.unit.type.PixelUnit
import net.prismclient.aether.ui.unit.type.RelativeUnit

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
 * composable's parent, or the width / height of the screen. If composable or it's parent is null. This
 * is considered a dynamic unit.
 *
 * @see RelativeUnit
 */
@get:JvmName("rel")
inline val Number.rel: RelativeUnit get() = RelativeUnit(this.toFloat())

/**
 * Creates a [ComposableRelativeUnit] of the given value. A composable relative unit scales based on
 * the width / height of the composable which is passed through on update. This is considered a dynamic unit.
 *
 * @see ComposableRelativeUnit
 */
@get:JvmName("crel")
inline val Number.crel: ComposableRelativeUnit get() = ComposableRelativeUnit(this.toFloat())

/**
 * Creates a [Radius] with the given value for each corner.
 */
@get:JvmName("radius")
inline val Number.radius: Radius get() = Radius(this.toFloat(), this.toFloat(), this.toFloat(), this.toFloat())

/**
 * Creates a [Radius] with the given pixel value
 */
@get:JvmName("radii")
inline val PixelUnit.radii: Radius get() = Radius(this.value)

// -- Util -- //

/**
 * Returns the cached value of the given unit, or 0f if the unit is null.
 */
@get:JvmName("dp")
inline val UIUnit<*>?.dp: Float get() = this?.cachedValue ?: 0f


// -- Operator Functions -- //

operator fun UIUnit<*>.plus(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.ADD)

operator fun UIUnit<*>.minus(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.SUBTRACT)

operator fun UIUnit<*>.times(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.MULTIPLY)

operator fun UIUnit<*>.div(other: UIUnit<*>): UIUnit<*> = OperationUnit(this, other, OperationUnit.Operation.DIVIDE)

operator fun Number.plus(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.ADD)

operator fun Number.minus(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.SUBTRACT)

operator fun Number.times(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.MULTIPLY)

operator fun Number.div(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.DIVIDE)