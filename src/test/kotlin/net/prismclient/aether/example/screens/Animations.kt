package net.prismclient.aether.example.screens

import net.prismclient.aether.core.animation.Keyframe
import net.prismclient.aether.core.animation.type.PropertyAnimation
import net.prismclient.aether.core.ease.impl.UIQuart
import net.prismclient.aether.core.util.shorthands.ColorOf
import net.prismclient.aether.core.util.shorthands.px
import net.prismclient.aether.core.util.shorthands.rgb
import net.prismclient.aether.example.Runner
import net.prismclient.aether.ui.alignment.Alignment
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

        val propertyAnimation = PropertyAnimation<UIButton, DefaultModifier>()

        val title = Label(
            text = "Some text!",
            modifier = Modifier()
                .control(Alignment.CENTER)
                .padding(5.px, 14.px, 5.px, 14f.px),
            fontStyle = FontStyle()
                .fontName("Poppins-Medium")
                .fontColor(0xFFFFFF.rgb)
                .fontSize(24.px)
                .fontType(FontType.AutoWidth),
            onClick = {
                propertyAnimation.start(this)
            }
        )

        propertyAnimation.keyframes.add(Keyframe(UIQuart(1250L), DefaultModifier()))
//        propertyAnimation.keyframes.add(Keyframe(UILinear(10000L), DefaultModifier().x(50.px)))
        propertyAnimation.keyframes.add(Keyframe(UIQuart(100L), DefaultModifier().backgroundColor(ColorOf(1f, 0f, 0f))))

        title.animations = HashMap()
        title.animations!!["Hello"] = propertyAnimation
    }
}