package net.prismclient.aether.ui

import net.prismclient.aether.ui.unit.type.PixelUnit
import net.prismclient.aether.ui.util.shorthands.*

class fiddle {
    fun func() {
        otherFunction(otherValue = 1f)
//        val value = 50.px
//        val otherValue = 50 + 50.px
//
//
//        compose("composition") {
//            button(
//                style = Style("") {
//                    background = UIBackground()
//                        .backgroundColor(Color.red)
//                        .backgroundRadius(10.radius)
//                        //.backgroundImage()
//                }
//            )
//        }
    }

    @JvmOverloads
    fun otherFunction(value1: String = "Hello world!", value2: String = "Hello planet!", value3: String = "Hello earth!", otherValue: Float) {
        println(value1)
        println(otherValue)
    }
}