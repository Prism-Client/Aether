package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.util.shorthands.Block
import net.prismclient.aether.ui.util.shorthands.asRGBA
import net.prismclient.aether.ui.util.shorthands.dp

open class UIButton(text: String, modifier: Modifier) : UIComponent<UIButton>(modifier) {
    var text: String = text
        // TODO: UIButton setter

    override fun renderComponent() {
        renderer {
            color(asRGBA(0f, 0f, 1f, 0.3f))
            rect(x.dp, y.dp, width.dp, height.dp)
        }
    }

    override fun copy(): UIButton = UIButton(text, modifier)
}

@JvmOverloads
fun button(text: String, modifier: Modifier = Modifier(), block: Block<UIButton> = {}): UIButton = UIButton(text, modifier).also(block)

