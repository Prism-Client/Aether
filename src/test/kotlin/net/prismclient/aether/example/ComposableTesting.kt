package net.prismclient.aether.example

import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.example.screens.PrismDesign
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.HorizontalAlignment
import net.prismclient.aether.ui.component.*
import net.prismclient.aether.ui.composition.CompositionModifier
import net.prismclient.aether.ui.composition.util.UIBackground
import net.prismclient.aether.ui.dsl.Compose
import net.prismclient.aether.ui.dsl.Resource
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.layout.BoxLayoutStyle
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.layout.scroll.Scrollbar
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.screen.UIScreen
import javax.swing.LayoutStyle

object ComposableTesting : UIScreen {
    @JvmStatic
    fun main(args: Array<String>) {
        Runner(ComposableTesting)
    }

    override fun compose() = Compose {
        if (!PrismDesign.initialized) {
            Resource {
                fontCollection(localResource("/fonts/Poppins"))
                fontCollection(localResource("/fonts/Montserrat"))

                pngCollection(localResource("/images/"))
                svgCollection(localResource("/icons/"))
            }
            PrismDesign.initialized = true
        }

        Composition(
            name = "Toplevel-Composition",
            modifier = CompositionModifier()
                .background(0xF5F5F7.rgb, 9.radius)
                .control(Alignment.CENTER)
                .size(500.px, 500.px)
        ) {
            Label(
                text = "The title.",
                modifier = Modifier()
                    /* Offset by the T character */
                    .position(39 - 6.px, 39.px),
                fontStyle = FontStyle()
                    .fontName("Montserrat-Bold")
                    .fontColor(0x292D32.rgb)
                    .fontSize(32.px)
                    .fontType(FontType.AutoWidth)
            )
            Label(
                text = "An amazing title and description can go a long way...",
                modifier = Modifier()
                    .position(39.px, (39 + 32).px),
                fontStyle = FontStyle()
                    .fontName("Montserrat-Regular")
                    .fontSize(12.px)
                    .width(400.px)
                    .fontColor(0x697483.rgb)
                    .fontType(FontType.FixedSize)
            )

            Column(
                horizontalAlignment = HorizontalAlignment.CENTER,
                modifier = LayoutModifier()
                    .control(Alignment.BOTTOMCENTER)
                    .y(1.rel - 20.px)
                    .background(0xFFFFFF.rgb, 9.radius),
                layoutStyle = BoxLayoutStyle()
                    .spacing(14.px)
            ) {
                modifier.size(1.rel - 40.px, 0.7.rel).optimizeComposition = true
                modifier.verticalScrollbar = Scrollbar {
                    x = 1.crel - 10.px
                    y = 0.025.crel
                    width = 5.px
                    height = 0.95.crel
                    thumbColor = 0x434343.rgb
                    thumbRadius = 2.5.radius

                    val scrollbarBackground = UIBackground()

                    scrollbarBackground.backgroundColor = RGBA(0f, 0f, 0f, 0.15f).rgba
                    scrollbarBackground.backgroundRadius = 2.5.radius

                    background = scrollbarBackground
                }

                for (i in 0 .. 10) {
                    Button(
                        text = "Test",
                        modifier = Modifier()
                            .padding(9.px, 54.px, 9.px, 54.px)
                            .background(RGBA(0f, 0f, 0f, 0.3f).rgba, 10.radius),
                        fontStyle = FontStyle()
                            .fontName("Montserrat-Regular")
                            .fontSize(16.px)
                            .fontColor(0xFFFFFF.rgb)
                            .fontType(FontType.AutoWidth)
                    )
                }

                Column(
                    horizontalAlignment = HorizontalAlignment.CENTER,
                    modifier = LayoutModifier()
                        .background(RGBA(1f, 0f, 0f, 0.1f).rgba, 9.radius)
                        .control(Alignment.BOTTOMCENTER)
                        .y(1.rel - 20.px),
                    layoutStyle = BoxLayoutStyle()
                        .spacing(14.px)
                ) {
                    modifier.size(1.rel - 40.px, 0.7.rel).optimizeComposition = true
                    modifier.verticalScrollbar = Scrollbar {
                        x = 1.crel - 10.px
                        y = 0.025.crel
                        width = 5.px
                        height = 0.95.crel
                        thumbColor = 0x434343.rgb
                        thumbRadius = 2.5.radius

                        val scrollbarBackground = UIBackground()

                        scrollbarBackground.backgroundColor = RGBA(0f, 0f, 0f, 0.15f).rgba
                        scrollbarBackground.backgroundRadius = 2.5.radius

                        background = scrollbarBackground
                    }

                    for (i in 0 .. 10) {
                        Button(
                            text = "Test",
                            modifier = Modifier()
                                .padding(9.px, 54.px, 9.px, 54.px)
                                .background(RGBA(0f, 0f, 0f, 0.3f).rgba, 10.radius),
                            fontStyle = FontStyle()
                                .fontName("Montserrat-Regular")
                                .fontSize(16.px)
                                .fontColor(0xFFFFFF.rgb)
                                .fontType(FontType.AutoWidth)
                        )
                    }
                }
            }
        }
    }
}