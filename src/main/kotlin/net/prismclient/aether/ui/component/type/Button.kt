package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.font.Font
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.font.UIFont
import net.prismclient.aether.ui.modifier.UIModifier

/**
 * @author sen
 * @since 1.0
 */
open class UIButton internal constructor(
    text: String,
    modifier: UIModifier<*>,
    override val font: UIFont
) : UIComponent<UIButton>(modifier), Font {
    constructor(text: String, modifier: UIModifier<*>, fontStyle: FontStyle) : this(text, modifier, UIFont(fontStyle))

    open var text: String = text
        set(value) {
            field = value
            font.actualText = value
            // TODO: compose update
        }

    override fun update() {
        font.actualText = text
        font.compose(this)
        if (dynamic)
            composePosition()
    }

    override fun renderComponent() {
        font.render()
    }

    override fun copy(): UIButton = UIButton(text, modifier.copy(), font.copy())
}