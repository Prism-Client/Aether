//package net.prismclient.aether.core.animation
//
//import net.prismclient.aether.ui.component.UIComponent
//
//enum class AnimationDirection {
//    OBVERSE, REVERSE
//}
//
//// TODO: Determine animation type
//class Animation<C : UIComponent<C>>(val component: C) {
//
//    var paused: Boolean = false
//
//    var duration: Long = 0L
//
//    var startTime: Long = 0L
//        private set
//
//    var remainingTime: Long = 0L
//        private set
//
//    var endTime: Long = 0L
//        private set
//
//    var direction: AnimationDirection = AnimationDirection.OBVERSE
//        set(value) {
//            field = value
//            internalReverse()
//        }
//
//    fun begin() {
//        remainingTime = duration
//        startTime = System.currentTimeMillis()
//        endTime = startTime + remainingTime
//    }
//
//    fun pause() {
//        paused = true
//        endTime = 0L
//    }
//
//    fun resume() {
//        endTime = startTime + remainingTime
//    }
//
//    fun reverse() {
//        direction = if (direction == AnimationDirection.OBVERSE) AnimationDirection.REVERSE else AnimationDirection.OBVERSE
//    }
//
//    /**
//     * Process reverse logic.
//     */
//    private fun internalReverse() {
//        remainingTime = duration - remainingTime
//    }
//
//    fun doAnimation() {
//        val currentTime = System.currentTimeMillis()
//        val hasFinished = remainingTime <= 0L
//
//        if (!paused && !hasFinished) {
//            // calculate the new remaining time of the animation.
//            remainingTime = endTime - currentTime
//        }
//
//        val progress = (duration / remainingTime).toFloat()
//
//
//    }
//
//}