package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.component.UIComponent
import net.prismclient.aether.ui.dsl.UIPathDSL
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.renderer.UIFramebuffer
import net.prismclient.aether.ui.util.shorthands.asRGBA
import net.prismclient.aether.ui.util.shorthands.px

/**
 * A composition is a group of components which fills a portion or full area of the screen. Compositions allow
 * for Aether to streamline component layout calculations and cache frames to improve the overall speed from initial
 * creation to actual render times (frames) of your UI.
 *
 * @author sen
 * @since 1.0
 */
open class UIComposition : Composable() {
    val components: ArrayList<UIComponent<*>> = arrayListOf()
    var framebuffer: UIFramebuffer? = null
        protected set

    /**
     * True by default. The composition will use a framebuffer.
     */
    var optimizeComposition: Boolean = true

    // -- Core -- //

    override fun compose() {
        if (!composed || dynamic) {
            // Compose all static components
            components.filterNot(UIComponent<*>::dynamic).forEach(UIComponent<*>::compose)

            updatePosition()
            updateSize()

            // Compose all dynamic components after the initial composition has been created.
            if (dynamic)
                components.filter(UIComponent<*>::dynamic).forEach(UIComponent<*>::compose)
            rasterize()
        }
    }

    override fun render() {
        renderer {
            if (optimizeComposition) {
                color(-1)
                rect(+x, +y, +width, +height)
                path {
                    imagePattern(framebuffer!!.imagePattern, +x, +y, +width, +height, 0f, 1f)
                    rect(+x, +y, +width, +height)
                }.fillPaint()
            }
        }
    }


    /**
     * Converts the active composition layout to a raster image.
     */
    open fun rasterize() {
        if (!optimizeComposition) return

        framebuffer = framebuffer ?: Aether.renderer.createFBO(+width, +height)

        UIRendererDSL.renderToFramebuffer(framebuffer!!) {
                components.forEach(UIComponent<*>::render)
        }
    }

    // -- Shorthands -- //

}