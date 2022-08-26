package net.prismclient.aether.ui.dsl

import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.component.type.Construct
import java.lang.RuntimeException

/**
 * Introduces a Kotlin DSL for the [Construct] component which allows creating them much easier.
 *
 * @author sen
 * @since 1.0
 */
object ConstructionDSL {
    var activeConstructor: Construct? = null

    /**
     * Sets the [Construct.action] to the [block] and executes it every frame. The [block] provides
     * access to all of [UIRendererDSL] which allows you to use the functions it provides.
     */
    fun render(block: Block<UIRendererDSL>) {
        check()
        activeConstructor!!.action = Runnable {
            UIRendererDSL.block()
        }
    }

    /**
     * Throws a runtime exception if the active constructor is null.
     */
    fun check() {
        if (activeConstructor == null)
            throw RuntimeException("The active constructor of ConstructionDSL is null. A construct must be defined with Components.kt.construct")
    }
}