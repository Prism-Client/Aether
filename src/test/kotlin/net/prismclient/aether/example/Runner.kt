package net.prismclient.aether.example

import net.prismclient.aether.core.Aether
import net.prismclient.aether.example.screens.TestScreen

object Runner : Game("Runner") {
    @JvmStatic
    fun main(args: Array<String>) {
        initialize()

        Aether.displayScreen(TestScreen())

        run()
    }
}