package net.prismclient.aether.core.debug

enum class LogLevel {
    DEBUG,
    GLOBAL
}

internal inline fun timed(message: String, level: LogLevel = LogLevel.DEBUG, block: () -> Unit) {
    if (level != LogLevel.DEBUG) {
        println("[UIDebug]: TIMED -> $message")
        val start = System.currentTimeMillis()
        block()
        println("[UIDebug]: TIMED -> took ${System.currentTimeMillis() - start}ms")
    }
}

internal fun inform(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (level != LogLevel.DEBUG)
        println("[UIDebug]: INFO -> $message")
}

internal fun debug(message: String) {
    println("[UIDebug]: DEBUG -> $message")
}

internal fun warn(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (level != LogLevel.DEBUG)
        println("[UIDebug]: WARNING -> $message")
}

internal fun error(message: String) {
    println("[UIDebug]: ERR -> $message")
}