package net.prismclient.aether.ui.composition.component.type

import net.prismclient.aether.ui.composition.component.UIComponent
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.util.shorthands.Block
import net.prismclient.aether.ui.util.shorthands.asRGBA

class UIButton : UIComponent<UIButton>() {
    override fun renderComponent() {
        renderer {
            color(asRGBA(0f, 0f, 1f, 0.3f))
            rect(+x, +y, +width, +height)
        }
    }

    override fun clone(): UIButton = UIButton()
}

fun button(block: Block<UIButton>): UIButton = UIButton().also(block)