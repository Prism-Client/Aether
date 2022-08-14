package net.prismclient.aether.ui.dsl

import net.prismclient.aether.core.util.shorthands.Block

inline fun renderer(block: Block<UIRendererDSL>) = UIRendererDSL.block()