package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.font.UIFont
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.util.shorthands.Block

open class UIButton internal constructor(
    text: String,
    modifier: Modifier,
    val font: UIFont
) : UIComponent<UIButton>(modifier) {
    constructor(text: String, modifier: Modifier, fontStyle: FontStyle) : this(text, modifier, UIFont(fontStyle))

    var text: String = text
        set(value) {
            field = value
            font.actualText = value
        }

    override fun update() {
        font.actualText = text
        font.update(this)
    }

    override fun renderComponent() {
        font.render()
    }

    override fun copy(): UIButton = UIButton(text, modifier, font.copy())
}

@JvmOverloads
fun button(
    text: String,
    modifier: Modifier = Modifier(),
    fontStyle: FontStyle = FontStyle(),
    block: Block<UIButton> = {}
): UIButton = UIButton(text, modifier, fontStyle).also(block)