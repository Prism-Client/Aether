package net.prismclient.aether.ui.screen

/**
 * @author sen
 * @since 1.0
 */
@JvmDefaultWithoutCompatibility
interface UIScreen {

    /**
     * Invoked when Aether has prepared the screen.
     */
    fun createScreen()
}

interface CloseableScreen : UIScreen {

    /**
     * Invoked when Aether has closed the screen.
     */
    fun closeScreen()
}