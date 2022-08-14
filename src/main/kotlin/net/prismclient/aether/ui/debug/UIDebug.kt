package net.prismclient.aether.ui.debug

import net.prismclient.aether.ui.dsl.renderer
import net.prismclient.aether.core.color.UIColor
import net.prismclient.aether.ui.renderer.UIStrokeDirection
import net.prismclient.aether.core.util.shorthands.RGBA
import net.prismclient.aether.core.util.shorthands.rgb

/**
 * [UIDebug] provides utility functions to aid in visualizing and obtaining more information
 * to find bugs within your UI. Invoke [UIDebug.debug] to initialize it. If logging is disabled,
 * it is automatically enabled.
 *
 * @author sen
 * @since 1.0
 */
object UIDebug { // TODO: Rewrite
    @JvmStatic internal var debug = false

    /**
     * When true, all statements, warning, error will be ignored.
     */
    @JvmStatic var disableLog: Boolean = false

    @JvmStatic var showBounds = true
    @JvmStatic var boundWidth: Float = 1f
    @JvmStatic var composition: UIColor = RGBA(0, 0, 255).rgb
    @JvmStatic var component: UIColor = RGBA(255, 0, 0).rgb
    @JvmStatic var font: UIColor = RGBA(0, 255, 0).rgb

    init {
        disableLog = false
        warn("Debug mode has been enabled.")
    }

    fun debug() {
        debug = true
    }

    fun disableDebug() {
        debug = false
    }

    internal fun renderBounds(x: Float, y: Float, width: Float, height: Float, color: UIColor) {
        if (!showBounds || !debug) return
        renderer {
            stroke(boundWidth, color.rgba, UIStrokeDirection.OUTSIDE) {
                rect(x, y, width, height)
            }
        }
    }
}