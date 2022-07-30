package net.prismclient.aether.ui.event

interface UIMouseMoveListener {
    fun onMouseMove(mouseX: Float, mouseY: Float)
}

interface UIMouseClickListener {
    fun onMouseClick(mouseX: Float, mouseY: Float, mouseButton: Int)
}

interface UIMouseReleaseListener {
    fun onMouseRelease(mouseX: Float, mouseY: Float, mouseButton: Int)
}

interface UIMouseScrollListener {
    fun onMouseScroll(scrollAmount: Float)
}

