package net.prismclient.aether.core.animation

import net.prismclient.aether.core.ease.UIEase
import net.prismclient.aether.ui.modifier.UIModifier

class Keyframe<T : UIModifier<T>>(val ease: UIEase, val modifier: UIModifier<T>)