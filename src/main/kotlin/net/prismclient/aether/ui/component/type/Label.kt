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
class Label internal constructor(
    text: String,
    modifier: UIModifier<*>,
    override val font: UIFont
) : UIComponent<Label>(modifier), Font {
    constructor(text: String, modifier: UIModifier<*>, fontStyle: FontStyle) : this(text, modifier, UIFont(fontStyle))

    var text: String = text
        set(value) {
            field = value
            font.actualText = value
            // Require recompose
        }

    override fun update() {
        font.actualText = text
        font.compose(this)
        if (dynamic) {
            composePosition()
            font.initialX = x
            font.initialY = y
            // TODO: Updating the initial x as the position might be updated after
        }
    }

    override fun renderComponent() {
        font.render()
    }

    override fun copy(): Label = Label(text, modifier.copy(), font.copy())
}