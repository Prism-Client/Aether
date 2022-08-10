package net.prismclient.aether.ui.debug

enum class LogLevel {
    DEBUG,
    GLOBAL
}

internal inline fun timed(message: String, level: LogLevel = LogLevel.DEBUG, block: () -> Unit) {
    if (UIDebug.disableLog) return
    if (level != LogLevel.DEBUG) {
        println("[UIDebug]: TIMED -> $message")
        val start = System.currentTimeMillis()
        block()
        println("[UIDebug]: TIMED -> took ${System.currentTimeMillis() - start}ms")
    }
}

internal fun inform(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (UIDebug.disableLog) return
    if (level != LogLevel.DEBUG)
        println("[UIDebug]: INFO -> $message")
}

internal fun debug(message: String) {
    if (UIDebug.disableLog) return
    println("[UIDebug]: DEBUG -> $message")
}

internal fun warn(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (UIDebug.disableLog) return
    if (level != LogLevel.DEBUG)
        println("[UIDebug]: WARNING -> $message")
}

internal fun error(message: String) {
    if (UIDebug.disableLog) return
    println("[UIDebug]: ERR -> $message")
}