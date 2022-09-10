package net.prismclient.aether.example.screens

import net.prismclient.aether.core.animation.Keyframe
import net.prismclient.aether.core.animation.type.PropertyAnimation
import net.prismclient.aether.core.ease.impl.UILinear
import net.prismclient.aether.core.util.shorthands.ColorOf
import net.prismclient.aether.core.util.shorthands.RGBA
import net.prismclient.aether.core.util.shorthands.px
import net.prismclient.aether.core.util.shorthands.rgb
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.composable.Label
import net.prismclient.aether.ui.dsl.Compose
import net.prismclient.aether.ui.dsl.Resource
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.modifier.DefaultModifier
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.screen.UIScreen

object Animations : UIScreen {
    var initialized = false

    @JvmStatic
    fun main(args: Array<String>) {
        Runner(Animations)
    }

    override fun compose() = Compose {
        if (!initialized) {
            Resource {
                fontCollection(localResource("/fonts/Poppins"))
                fontCollection(localResource("/fonts/Montserrat"))

                pngCollection(localResource("/images/"))
                svgCollection(localResource("/icons/"))
            }
            initialized = true
        }

        val title = Label(
            text = "Some text!",
            modifier = Modifier()
                .position(0.px, 29.px),
            fontStyle = FontStyle()
                .fontName("Poppins-Medium")
                .fontColor(0xFFFFFF.rgb)
                .fontSize(24.px)
                .fontType(FontType.AutoWidth)
        )

        val propertyAnimation = PropertyAnimation<UIButton, DefaultModifier>()

        propertyAnimation.keyframes.add(Keyframe(UILinear(1000L), DefaultModifier()))
        propertyAnimation.keyframes.add(Keyframe(UILinear(1000L), DefaultModifier().backgroundColor(ColorOf(1f ,0f ,0f))))
        propertyAnimation.keyframes.add(Keyframe(UILinear(1000L), DefaultModifier().x(50.px)))

        title.animations = HashMap()
        title.animations!!["Hello"] = propertyAnimation

        propertyAnimation.start(title)
    }
}