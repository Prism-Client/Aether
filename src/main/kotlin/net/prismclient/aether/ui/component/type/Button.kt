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
    @Suppress("LeakingThis")
    open val fontStyle: FontStyle = font.style

    constructor(text: String, modifier: UIModifier<*>, fontStyle: FontStyle) : this(text, modifier, UIFont(fontStyle))

    open var text: String = text
        set(value) {
            field = value
            font.actualText = value
            // TODO: compose update
        }

    override fun composePadding() {
        font.actualText = text
        // Calculate the font metrics, and update the size. Compose
        // Padding is invoked directly after updating the width.
        font.composeSize(this)
        super.composePadding()
    }

    override fun update() {
        // After the position is calculated compose the font
        font.compose(this)

//        font.compose(this)
//        if (dynamic)
//            composePosition()
    }

    override fun renderComponent() {
        font.render()
    }

    override fun copy(): UIButton = UIButton(text, modifier.copy(), font.copy())
}