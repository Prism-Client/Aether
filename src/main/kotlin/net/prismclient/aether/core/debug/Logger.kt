package net.prismclient.aether.core.debug

enum class LogLevel {
    DEBUG,
    GLOBAL
}

@PublishedApi
internal inline fun timed(message: String, level: LogLevel = LogLevel.DEBUG, block: () -> Unit) {
    if (level != LogLevel.DEBUG) {
        println("[UIDebug]: TIMED -> $message")
        val start = System.currentTimeMillis()
        block()
        println("[UIDebug]: TIMED -> took ${System.currentTimeMillis() - start}ms")
    }
}

@PublishedApi
internal fun inform(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (level != LogLevel.DEBUG)
        println("[UIDebug]: INFO -> $message")
}

@PublishedApi
internal fun debug(message: String) {
    println("[UIDebug]: DEBUG -> $message")
}

@PublishedApi
internal fun warn(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (level != LogLevel.DEBUG)
        println("[UIDebug]: WARNING -> $message")
}

@PublishedApi
internal fun error(message: String) {
    println("[UIDebug]: ERR -> $message")
}