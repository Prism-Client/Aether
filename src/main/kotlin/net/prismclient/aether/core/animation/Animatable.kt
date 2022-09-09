package net.prismclient.aether.core.animation

import net.prismclient.aether.ui.composition.Composable
import net.prismclient.aether.ui.modifier.UIModifier

interface Animatable<C : Composable, M : UIModifier<*>> {
    var animations: HashMap<String, Animation<C, M>>?
}