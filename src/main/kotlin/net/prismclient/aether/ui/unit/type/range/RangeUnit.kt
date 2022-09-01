package net.prismclient.aether.ui.unit.type.range

import net.prismclient.aether.ui.unit.UIUnit

class RangeUnit(var unit: UIUnit<*>, var min: UIUnit<*>?, var max: UIUnit<*>?) : UIUnit<RangeUnit>(0f) {
}