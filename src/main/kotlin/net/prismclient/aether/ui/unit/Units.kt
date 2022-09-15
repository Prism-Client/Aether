package net.prismclient.aether.ui.unit

import net.prismclient.aether.core.util.property.Copyable
import net.prismclient.aether.ui.composition.Composable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * [UIUnit] is the superclass of all units, which represent an amount of pixels on the screen when
 * computed. It is used to describe the position, and size of components and shapes as well as other
 * pixel based properties. The provided generic value must be the class which is inheriting this class.
 * It is used for the interface [Copyable] so that the unit can be copied to be manipulated with later.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIUnit<T : UIUnit<T>>(open var value: Float) : Copyable<T> {
    open var cachedValue: Float = 0f

    /**
     * Updates the [cachedValue] based on the provided values.
     *
     * @see updateCache
     */
    open fun compute(composable: Composable?, width: Float, height: Float, yaxis: Boolean) {
        cachedValue = updateCache(composable, width, height, yaxis)
    }

    /**
     * Updates [cachedValue] based on the given value. The [width] and [height] represent the
     * width and height of the composable or other object, such as a shape. [yaxis] indicates
     * that the expected axis should be the y-axis.
     */
    abstract fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float

    /**
     * [identifiesAs] is intended to replace the `instanceof` or `is` keyword as certain [UIUnit]s might
     * not actually be an instance of the expected type, but represent one, such as a [DynamicUnit].
     */
    open fun <T : UIUnit<T>> identifiesAs(clazz: KClass<T>): Boolean = this::class.isSubclassOf(clazz)
}

/**
 * An internal class used to represent a [Composable] which resizes based on the content within it.
 *
 * @author sen
 * @since 1.0
 */
internal class ResizeUnit : UIUnit<ResizeUnit>(0f) {
    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float = 0f

    override fun copy(): ResizeUnit = ResizeUnit()
}

/**
 * Computes and updates the unit with the provided [composable] as the width and height.
 */
internal fun UIUnit<*>?.compute(composable: Composable?, yaxis: Boolean) {
    this?.compute(composable, composable?.width ?: 0f, composable?.height ?: 0f, yaxis)
}

/**
 * An extension function used to support the [UIUnit.identifiesAs] function. Returns true if this
 * is a subclass or equal to [other], like an instanceof type check.
 */
internal fun <T : UIUnit<T>> UIUnit<*>?.typeOf(other: KClass<T>): Boolean =
    this != null && (this.identifiesAs(other))