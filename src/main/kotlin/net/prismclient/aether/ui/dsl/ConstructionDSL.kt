package net.prismclient.aether.ui.dsl

import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.component.type.DefaultConstruct

/**
 * Introduces a Kotlin DSL for the [DefaultConstruct] component which allows creating them much easier.
 *
 * @author sen
 * @since 1.0
 */
object ConstructionDSL {
    var activeConstructor: DefaultConstruct? = null

    /**
     * Sets the [DefaultConstruct.action] to the [block] and executes it every frame. The [block] provides
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