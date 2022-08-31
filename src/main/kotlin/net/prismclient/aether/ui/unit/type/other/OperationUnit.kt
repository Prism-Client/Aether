package net.prismclient.aether.ui.unit.type.other

import net.prismclient.aether.core.util.shorthands.dp
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.unit.UIUnit
import kotlin.reflect.KClass

/**
 * [OperationUnit] itself, is not a value, however, it accepts two units which it computes
 * a value based on the value of the given units. Because [OperationUnit] accepts units as
 * values to operate on, it can be considered a dynamic or static unit depending on the
 * provided units. If at least one unit is dynamic this is dynamic.
 *
 * @author sen
 * @since 1.0
 */
@Suppress("MemberVisibilityCanBePrivate")
open class OperationUnit(val unit1: UIUnit<*>, val unit2: UIUnit<*>, val operation: Operation) :
        UIUnit<OperationUnit>(0f) {

    override fun updateCache(composable: Composable?, width: Float, height: Float, yaxis: Boolean): Float {
        // The units automatically make the composable dynamic if necessary.
        unit1.compute(composable, width, height, yaxis)
        unit2.compute(composable, width, height, yaxis)
        return when (operation) {
            Operation.ADD -> unit1.dp + unit2.dp
            Operation.SUBTRACT -> unit1.dp - unit2.dp
            Operation.MULTIPLY -> unit1.dp * unit2.dp
            Operation.DIVIDE -> unit1.dp / unit2.dp
        }
    }

    override fun <T : UIUnit<T>> identifiesAs(clazz: KClass<T>): Boolean =
            super.identifiesAs(clazz) || unit1.identifiesAs(clazz) || unit2.identifiesAs(clazz)

    override fun copy(): OperationUnit = OperationUnit(unit1.copy(), unit2.copy(), operation)

    override fun toString(): String = "Operation($unit1, $unit2, $operation)"

    /**
     * Represents an operation that [OperationUnit] does when the value needs to be computed.
     *
     * @author sen
     * @since 1.0
     */
    enum class Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }
}