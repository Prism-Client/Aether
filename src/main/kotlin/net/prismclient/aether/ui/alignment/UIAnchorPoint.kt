package net.prismclient.aether.ui.alignment

import net.prismclient.aether.ui.unit.UIUnit

/**
 * [UIAnchorPoint] contains two units, [x], and [y] which represent the x and y coordinates of the
 * anchor point of a composable. Relative values scale based on the size of the composable.
 *
 * @author sen
 * @since 1.0
 */
class UIAnchorPoint(var x: UIUnit? = null, var y: UIUnit? = null)