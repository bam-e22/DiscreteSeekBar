package io.github.stack07142.discreteseekbar

import android.graphics.drawable.Drawable
import android.util.SparseArray

class ConfigBuilder(discreteSeekBar: DiscreteSeekBar) {

    // value
    var minValue: Int
    var maxValue: Int
    var sectionCount: Int
    var value: Int = 0

    // track
    val trackWidth: Int
    val trackColor: Int

    // tickMark
    var tickMarkTextArray = SparseArray<String>()
    val tickMarkDrawable: Drawable?
    val tickMarkTextTopMargin: Int
    val tickMarkTextSize: Int
    val tickMarkTextColor: Int

    // thumb
    val thumbDefaultRadius: Int
    val thumbPressedRadius: Int
    val thumbColor: Int

    // touch event
    var onValueChangedListener: DiscreteSeekBar.OnValueChangedListener? = null
    val trackTouchEnable: Boolean


}