package net.prismclient.aether.ui.component

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.composition.UIComposition
import net.prismclient.aether.ui.unit.UIUnit


/**
 * @author sen
 * @since 1.0
 */
abstract class UIComponent {
    var composition: UIComposition? = Aether.instance.activeComposition

    var parent: UIComponent? = null

    val parentWidth: Float
        get() = 0f
    val parentHeight: Float
        get() = 0f
    var isDynamic: Boolean = false
}