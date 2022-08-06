package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.font.UIFont
import net.prismclient.aether.ui.modifier.UIModifier

open class UIButton internal constructor(
    text: String,
    modifier: UIModifier<*>,
    val font: UIFont
) : UIComponent<UIButton>(modifier) {
    constructor(text: String, modifier: UIModifier<*>, fontStyle: FontStyle) : this(text, modifier, UIFont(fontStyle))

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

    override fun copy(): UIButton = UIButton(text, modifier.copy(), font.copy())
}