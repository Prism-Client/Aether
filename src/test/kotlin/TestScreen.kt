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
        // Composition Modifier
        register(
            CompositionModifier()
                .backgroundColor(0x202020.rgb)
                .backgroundRadius(25.radius)
        )

        // Font style
        register(
            FontStyle()
                .fontName("Poppins")
                .fontType(FontType.AutoWidth)
                .fontAlignment(UITextAlignment.LEFT, UITextAlignment.TOP)
                .offsetBaseline()
        )

        compose("Test") {
            modifier.constrain(50.px, 50.px, 500.px, 500.px)
                .control(UIAlignment.CENTER)
//                .optimizeComposition = false

            val lab = label(
                text = "RENEW",
                modifier = Modifier()
                    .constrain(25, 25, 204, 41)
                    .backgroundColor(0xE60000.rgb)
                    .backgroundRadius(5.radius),
                fontStyle = FontStyle()
                    .constrain(0.5.crel, 0.5.crel, 1.crel, 1.crel)
                    .fontAnchor(UIAlignment.CENTER)
                    .fontType(FontType.FixedSize)
                    .fontSize(16.px)
                    .fontColor(RGBA(255, 255, 255).rgb)
                    .offsetBaseline(false)
            )

            lab.onClick {
                lab.modifier.size(Random.nextInt(500), Random.nextInt(500))
                lab.modifier.backgroundColor(Random.nextInt().rgb)
                lab.modifier.backgroundRadius(Random.nextInt(50).radius)
            }

            val layout = component(UIListLayout(
                LayoutDirection.VERTICAL,
                LayoutOrder.FIRST,
                10.px,
                modifier = DefaultLayoutModifier()
                    .control(UIAlignment.CENTER)
                    .size(250, 250)
                    .anchor(UIAlignment.CENTER)
                    .backgroundColor(RGBA(255, 255, 255).rgb)
                    .backgroundRadius(9.radius).apply {
                        verticalScrollbar = DefaultScrollbar().apply {
                            overflow = Overflow.SCROLLBAR
                            x = 1.crel - 8.px
                            y = 0.05.crel
                            width = 8.px
                            height = 0.9.crel
                            thumbColor = RGBA(255, 0, 0).rgb
                        }
                    }
            )) {
                for (i in 0..10) {
                    val btn = button(
                        text = "AgdCiLMmzjt ${i * 10}",
                        modifier = Modifier()
                            .control(UIAlignment.CENTER)
                            .backgroundColor(0x29CC97.rgb)
                            .backgroundRadius(9.radius)
                            .padding(10, 0, 10, 0),
                        fontStyle = FontStyle()
                            .fontSize(16.px)
                            .fontColor(RGBA(255, 255, 255).rgb)
                    )

                    btn.onClick {
                        btn.modifier.backgroundColor(UIColor(RGBA(1f, 0f, 0f)))
                    }
                }
            }
        }
    }
}