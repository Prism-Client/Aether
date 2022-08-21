package net.prismclient.aether.core.util.shorthands

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.other.UIRadius
import net.prismclient.aether.ui.unit.type.dynamic.RelativeUnit
import net.prismclient.aether.ui.unit.type.dynamic.SizeUnit
import net.prismclient.aether.ui.unit.type.other.OperationUnit
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

/**
 * Disables units from changing the state of [Composable.dynamic].
 */
internal inline fun disableDynamicCheck(composable: Composable?, block: () -> Unit) {
    val isDynamic = composable?.dynamic ?: false
    block()
    composable?.dynamic = isDynamic
}


// -- Operator Functions -- //

operator fun UIUnit<*>.plus(other: UIUnit<*>): OperationUnit = OperationUnit(this, other, OperationUnit.Operation.ADD)

operator fun UIUnit<*>.minus(other: UIUnit<*>): OperationUnit = OperationUnit(this, other, OperationUnit.Operation.SUBTRACT)

operator fun UIUnit<*>.times(other: UIUnit<*>): OperationUnit = OperationUnit(this, other, OperationUnit.Operation.MULTIPLY)

operator fun UIUnit<*>.div(other: UIUnit<*>): OperationUnit = OperationUnit(this, other, OperationUnit.Operation.DIVIDE)

operator fun Number.plus(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.ADD)

operator fun Number.minus(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.SUBTRACT)

operator fun Number.times(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.MULTIPLY)

operator fun Number.div(other: UIUnit<*>): OperationUnit = OperationUnit(this.px, other, OperationUnit.Operation.DIVIDE)