package net.prismclient.aether.example.screens

import net.prismclient.aether.core.metrics.Size
import net.prismclient.aether.core.util.shorthands.*
import net.prismclient.aether.ui.alignment.Alignment
import net.prismclient.aether.ui.alignment.HorizontalAlignment
import net.prismclient.aether.ui.alignment.UITextAlignment
import net.prismclient.aether.ui.component.*
import net.prismclient.aether.ui.component.type.IconModifier
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.component.type.imageColor
import net.prismclient.aether.ui.composition.disableOptimizations
import net.prismclient.aether.ui.composition.onClick
import net.prismclient.aether.ui.controller.Control
import net.prismclient.aether.ui.font.*
import net.prismclient.aether.ui.layout.BoxLayoutStyle
import net.prismclient.aether.ui.layout.LayoutModifier
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.unit.other.UIRadius
import java.lang.Float.max

fun Dashboard() = PrismDesign.Pane {
    Label(
        text = "Dashboard",
        modifier = Modifier()
            .position(47.px, 29.px),
        fontStyle = FontStyle()
            .fontName("Poppins-Medium")
            .fontColor(0xFFFFFF.rgb)
            .fontSize(24.px)
            .fontType(FontType.AutoWidth)
    )
}

fun Mods() = PrismDesign.Pane {
    println("Hello!")

    fun CategoryButton(text: String): UIButton = Button(
        text = text,
        modifier = Modifier()
            .position(100, 25)
            .backgroundRadius(10.radius)
            .padding(10.px, 17.px, 10.px, 17.px),
        fontStyle = FontStyle()
            .fontName("Poppins-Regular")
            .fontSize(16.px)
            .fontColor(0x697483.rgb)
            .fontType(FontType.AutoWidth)
    )

    fun Module(name: String, iconName: String, enabled: Boolean, favorited: Boolean) = Box {
        modifier
            .size(230, 180)
            .backgroundColor(0xF4F9FF.rgb)
            .backgroundRadius(UIRadius(topLeft = 15.px, topRight = 15.px))
            .disableOptimizations()

        modifier.background!!.height = 1.crel - 39.px

        Icon(
            imageName = iconName,
            modifier = IconModifier()
                .control(Alignment.CENTER)
                .anchor(Alignment.TOPCENTER)
                .y(36.px)
                .size(69, 69)
                .imageColor(0x292D32.rgb)
        )

        Box(
            modifier = LayoutModifier()
                .backgroundColor(0x292D32.rgb)
                .backgroundRadius(UIRadius(bottomLeft = 15.px, bottomRight = 15.px))
        ) {
            modifier
                .size(1.rel, 39.px)
                .control(Alignment.BOTTOMLEFT)
                .disableOptimizations()
            Button(
                text = name,
                modifier = Modifier()
                    .control(Alignment.MIDDLELEFT)
                    .x(15.px),
                fontStyle = FontStyle()
                    .fontName("Montserrat-Medium")
                    .fontColor(0xF4F9FF.rgb)
                    .fontSize(15.px)
                    .fontType(FontType.AutoWidth)
                    .fontAlignment(UITextAlignment.LEFT, UITextAlignment.CENTER)
            )
            Icon(
                imageName = (if (favorited) "outline" else "solid") + "/star",
                modifier = IconModifier()
                    .control(Alignment.MIDDLERIGHT)
                    .x(1.rel - 15.px)
                    .size(16, 16)
                    .imageColor(0xFFFFFF.rgb)
            )
        }
    }

    var spacing = 0f

    Layout(
        modifier = LayoutModifier()
            .y(79.px)
            .size(1.rel, 1.rel - 79.px),
        layout = { children, _ ->
            spacing = 0f

            // Calculate the spacing
            run loop@{
                children.forEachIndexed { i, child ->
                    child.overridden = true
                    child.compose()
                    spacing += child.relWidth

                    if (spacing + child.relWidth > width) {
                        spacing = (width - spacing) / (i + 2)
                        return@loop
                    }
                }
            }

            var x = x + spacing
            var y = y
            var w = 0f
            var h = 0f

            children.forEach { child ->
                child.overridden = true
                child.compose()
                child.x = x
                child.y = y
                x += child.relWidth + spacing
                child.compose()

                if (x > width + this.x) {
                    x = this.x + spacing
                    y += child.relHeight + spacing
                }

                w = max(child.relX + child.relWidth - this.x, w)
                h = max(child.relY + child.relHeight - this.y, h)
            }

            return@Layout Size(w, h)
        }
    ) {
        modifier.verticalScrollbar = PrismDesign.Scrollbar()

        for (i in 0..20) {
            Module("CPS", "mods/mouse", false, false)
        }
    }

    Row(
        modifier = LayoutModifier()
            .position(47.px, 41.px) //.position(47.px + Dependent { title.width } + 50.px, 41.px)
            .anchor(Alignment.MIDDLELEFT),
        layoutStyle = BoxLayoutStyle()
            .spacing(43.px)
    ) {
        Control(
            selectedComposable = CategoryButton("All").apply {
                modifier.backgroundColor(0x292D32.rgb)
                fontStyle.fontColor(RGBA(255, 255, 255).rgba)
            }
        ) {
            children.add(CategoryButton("Performance"))
            children.add(CategoryButton("Server"))

            onSelect {
                it.modifier.backgroundColor(0x292D32.rgb)
                it.fontStyle.fontColor(RGBA(255, 255, 255).rgba)
            }
            onDeselect {
                it.modifier.backgroundColor(0.rgba)
                it.fontStyle.fontColor(0x697483.rgb)
            }
            children.forEach { btn -> btn.onClick { select(btn as UIButton) } }
        }
    }
}

fun NotSupported() = PrismDesign.Pane {
    Column(
        modifier = LayoutModifier().control(Alignment.CENTER),
        layoutStyle = BoxLayoutStyle().spacing(7.px)
    ) {
        Label(
            text = "Work in progress...",
            fontStyle = FontStyle()
                .fontName("Poppins-Medium")
                .fontColor(0xFFFFFF.rgb)
                .fontSize(32.px)
                .fontType(FontType.AutoWidth)
        )
        Label(
            text = "This window is work in progress :p \n\nSorry ~sen <3",
            fontStyle = FontStyle()
                .x(0.5.rel)
                .fontAlignment(UITextAlignment.CENTER, UITextAlignment.TOP)
                .fontName("Poppins-Regular")
                .fontColor(0xEFF6FF.rgb)
                .fontSize(14.px)
                .fontType(FontType.AutoHeight)
        )
    }
}
