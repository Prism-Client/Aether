package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.font.Font
import net.prismclient.aether.ui.font.FontStyle
import net.prismclient.aether.ui.font.UIFont
import net.prismclient.aether.ui.modifier.UIModifier

class UILabel internal constructor(
    text: String,
    modifier: UIModifier<*>,
    override val font: UIFont
) : UIComponent<UILabel>(modifier), Font {
    @Suppress("LeakingThis")
    open val fontStyle: FontStyle = font.style

    constructor(text: String, modifier: UIModifier<*>, fontStyle: FontStyle) : this(text, modifier, UIFont(fontStyle))

    open var text: String = text
        set(value) {
            field = value
            font.actualText = value
        }

    override fun composeSize() {
        super.composeSize()
        font.actualText = text
        // Calculate the font metrics, and update the size. Compose
        // Padding is invoked directly after updating the width.
        font.composeSize(this)
        modifier.composeAnchorPoint(this)
        modifier.composePadding(this)
        modifier.composeMargin(this)
    }


    override fun update() {
        // After the position is calculated compose the font
        font.compose(this)
    }

    override fun renderComponent() {
        font.render()
    }

    override fun copy(): UILabel = UILabel(text, modifier.copy(), font.copy())
}