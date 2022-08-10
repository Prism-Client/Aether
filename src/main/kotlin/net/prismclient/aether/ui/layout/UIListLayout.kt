package net.prismclient.aether.ui.layout

import net.prismclient.aether.ui.modifier.UIModifier

class UIListLayout(modifier: UIModifier<*>) : UILayout(modifier, true) {
    override fun updateLayout() {

        var w = x
        var h = y
        children.forEach {
            it.x = w
            it.y = h
            h += it.height
            it.compose()
        }
    }
}