package net.prismclient.aether.ui.dsl

import net.prismclient.aether.ui.util.shorthands.Block

inline fun renderer(block: Block<UIRendererDSL>) = UIRendererDSL.block()