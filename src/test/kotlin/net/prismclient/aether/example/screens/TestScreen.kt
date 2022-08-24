package net.prismclient.aether.example.screens

import net.prismclient.aether.core.Aether
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.alignment.UIAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.button
import net.prismclient.aether.ui.component.component
import net.prismclient.aether.ui.component.compose
import net.prismclient.aether.ui.component.label
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.onClick
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.layout.DefaultLayoutModifier
import net.prismclient.aether.ui.layout.UIListLayout
import net.prismclient.aether.ui.layout.scroll.DefaultScrollbar
import net.prismclient.aether.ui.layout.util.LayoutDirection
import net.prismclient.aether.ui.layout.util.LayoutOrder
import net.prismclient.aether.ui.layout.util.Overflow
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.registry.register
import net.prismclient.aether.ui.screen.UIScreen
import kotlin.random.Random

class TestScreen : UIScreen {
    override fun createScreen() {


        register(
            FontStyle()
                .fontName("Poppins")
                .fontType(FontType.AutoWidth)
                .fontAlignment(UITextAlignment.LEFT, UITextAlignment.TOP)
                .offsetBaseline()
        )

        compose(
            name = "Test",
            modifier = CompositionModifier()
                .constrain(50.px, 50.px, 500.px, 500.px)
                .backgroundColor(RGBA(0f, 1f, 0f).rgb)
        ) {

        }
    }
}