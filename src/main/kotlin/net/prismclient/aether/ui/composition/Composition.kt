package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.modifier.Modifier
import net.prismclient.aether.ui.renderer.UIFramebuffer
import net.prismclient.aether.ui.util.shorthands.RGBA
import net.prismclient.aether.ui.util.shorthands.dp

// TODO: disable optimize composition
/**
 * A composition is a group of components which fills a portion or full area of the screen. Compositions allow
 * for Aether to streamline component layout calculations and cache frames to improve the overall speed from initial
 * creation to actual render times (frames) of your UI.
 *
 * @author sen
 * @since 1.0
 */
open class Composition(modifier: Modifier) : Composable(modifier) {
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
            modifier.preUpdate(this)

            updatePosition()
            updateSize()

            // Compose all static components
            components.filterNot(UIComponent<*>::dynamic).forEach(UIComponent<*>::compose)

            // Compose all dynamic components after the initial composition has been created.
            if (dynamic)
                components.filter(UIComponent<*>::dynamic).forEach(UIComponent<*>::compose)
            modifier.update(this)
            rasterize()
        }
    }

    override fun render() {
        renderer {
            if (optimizeComposition) {
                color(-1)
                path {
                    imagePattern(framebuffer!!.imagePattern, x.dp, y.dp, width.dp, height.dp, 0f, 1f)
                    rect(x.dp, y.dp, width.dp, height.dp, modifier.background?.backgroundRadius)
                }.fillPaint()
            }
        }
    }


    /**
     * Converts the active composition layout to a raster image.
     */
    open fun rasterize() {
        if (!optimizeComposition) return

        framebuffer = framebuffer ?: Aether.renderer.createFBO(width.dp, height.dp)

        UIRendererDSL.renderToFramebuffer(framebuffer!!) {
            modifier.preRender()
            components.forEach(UIComponent<*>::render)
            modifier.render()
        }
    }

    // -- Shorthands -- //

}