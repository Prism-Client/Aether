package net.prismclient.aether.ui.composition

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.component.UIComponent
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.renderer.UIFramebuffer
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

            x?.compute(this, false)
            y?.compute(this, false)
            width?.compute(this, false)
            height?.compute(this, false)

            // Compose all dynamic components after the initial composition has been created.
            if (dynamic)
                components.filter(UIComponent<*>::dynamic).forEach(UIComponent<*>::compose)
            rasterize()
        }
    }

    override fun render() {
        renderer {
            if (optimizeComposition) {
                path {
                    color(-1)
                    imagePattern(framebuffer!!.imagePattern, )
                }.fillPath()
            }
        }
    }


    /**
     * Converts the active composition layout to a raster image.
     */
    open fun rasterize() {
        if (!optimizeComposition) return

        framebuffer = framebuffer ?: Aether.renderer.createFBO(width?.cachedValue ?: 0f, height?.cachedValue ?: 0f)

        UIRendererDSL.renderToFramebuffer(framebuffer!!) { render() }
    }

    // -- Shorthands -- //

    open fun constrain(x: Number, y: Number, width: Number, height: Number) =
        constrain(px(x), px(y), px(width), px(height))

    open fun constrain(x: UIUnit, y: UIUnit, width: UIUnit, height: UIUnit) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
    }
}