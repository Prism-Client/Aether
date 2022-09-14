package net.prismclient.aether

import net.prismclient.aether.core.util.shorthands.Block
import net.prismclient.aether.ui.component.type.UIButton
import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.dsl.UIRendererDSL


/**
 * used to test out design patterns and kotlin features
 */
class fiddle {
    fun <T : Composable> Control(block: Block<UIRendererDSL>) {

    }

    fun func() {
        /*
        compositions!!.forEach {
            it.composeInstructions { instructions -> instructions.composeAll(it) // it.children }
        }


        */
//        Control<UIButton>(
//            active = Button("HUD")
//        ) {
//            Button("Performance")
//            Button("Server")
//        }
        // Control<UIButton>(
    //
    // ) {
        //      button("Hello world!")
        // }
    }
}