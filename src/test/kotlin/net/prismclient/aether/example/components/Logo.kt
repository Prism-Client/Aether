package net.prismclient.aether.example.components

import net.prismclient.aether.core.util.shorthands.copy
import net.prismclient.aether.ui.component.type.Construct
import net.prismclient.aether.ui.component.type.DefaultConstruct
import net.prismclient.aether.ui.modifier.UIModifier

/**
 * A component to represent the component logo. This extends a construct, as it is rendering
 * a fixed set of shapes which is not intended to be changed.
 *
 * @author sen
 * @since 1.0
 */
class Logo(modifier: UIModifier<*>) : Construct<Logo>(modifier) {
    init {
        modifier.size(145, 37)
        action = logoAction
    }

    override fun copy(): Logo = Logo(modifier.copy)

    companion object {
        @JvmStatic
        val logoAction: Runnable = Runnable {

        }
    }
}