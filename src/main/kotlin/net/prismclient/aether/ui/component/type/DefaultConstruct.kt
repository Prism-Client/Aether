package net.prismclient.aether.ui.component.type

import net.prismclient.aether.core.util.shorthands.copy
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.ConstructionDSL
import net.prismclient.aether.ui.modifier.UIModifier

/**
 * [Construct] renders given instructions as a [UIComponent]. It allows for easy access
 * to render custom shapes without creating a custom class to handle it. It works by having
 * a [functional interface](https://kotlinlang.org/docs/fun-interfaces.html) which is executed
 * everytime it needs to be rendered. The functional interface, known as [action] is a [Runnable]
 * which simply is executed during [renderComponent]. This class removes the need for a lot of
 * simple boilerplate [UIComponent]s as drawing shapes becomes a lot easier.
 *
 * This is an abstract class, and the default implementation is [DefaultConstruct].
 *
 * @author sen
 * @since 1.0
 *
 * @see [net.prismclient.aether.ui.component.ComponentsKt.construct]
 * @see ConstructionDSL
 * @see DefaultConstruct
 */
abstract class Construct<T : Construct<T>>(modifier: UIModifier<*>) : UIComponent<T>(modifier) {
    // TODO: Property Constructs: A Construction which stores a sequence of properties
    // TODO: Construct database to remove the need for unnecessary action duplicates

    /**
     * Runs this [Runnable] every time the [UIComponent.renderComponent] function would normally be invoked.
     */
    open var action: Runnable? = null

    override fun renderComponent() {
        action?.run()
    }
}

/**
 * The default implementation of a [Construct] see [Construct] for more information
 *
 * @author sen
 * @since 1.0
 * @see Construct
 */
open class DefaultConstruct(modifier: UIModifier<*>) : Construct<DefaultConstruct>(modifier) {
    override fun copy(): DefaultConstruct = DefaultConstruct(modifier.copy()).also {
        it.action = action
    }
}