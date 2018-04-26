package io.github.stack07142.discreteseekbar

import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.SparseArray

class ConfigBuilder(private val discreteSeekBar: DiscreteSeekBar) {

    // value
    var minValue: Int = 0
    var maxValue: Int = 100
    var sectionCount: Int = 2
    var value: Int = 0

    // track
    var trackWidth: Int = 0
    var trackColor: Int = 0

    // tickMark
    var tickMarkTextTopMargin: Int = 0
    var tickMarkTextSize: Int = 0
    var tickMarkTextColor: Int = 0
    var tickMarkDrawable: Drawable? = null
    var tickMarkTextArray: SparseArray<String> = SparseArray()

    // thumb
    var thumbDefaultRadius: Int = 0
    var thumbPressedRadius: Int = 0
    var thumbColor: Int = 0

    // touch event
    var onValueChangedListener: DiscreteSeekBar.OnValueChangedListener? = null
    var trackTouchEnable: Boolean = false
    var touchAreaFactor: Float = 1.7f
    var animDuration: Long = 300L

    fun build() = discreteSeekBar.config(this)

    fun setMinValue(minValue: Int): ConfigBuilder {
        this.minValue = minValue
        return this
    }

    fun setMaxValue(maxValue: Int): ConfigBuilder {
        this.maxValue = maxValue
        return this
    }

    fun setSectionCount(sectionCount: Int): ConfigBuilder {
        this.sectionCount = sectionCount
        return this
    }

    fun setValue(value: Int): ConfigBuilder {
        this.value = value
        return this
    }

    fun setTrackWidth(dp: Int): ConfigBuilder {
        this.trackWidth = discreteSeekBar.dp2px(dp)
        return this
    }

    fun setTrackColor(@ColorRes colorRes: Int): ConfigBuilder {
        this.trackColor = ContextCompat.getColor(discreteSeekBar.context, colorRes)
        return this
    }

    fun setTickMarkTextTopMargin(dp: Int): ConfigBuilder {
        this.tickMarkTextTopMargin = discreteSeekBar.dp2px(dp)
        return this
    }

    fun setTickMarkTextSize(sp: Int): ConfigBuilder {
        this.tickMarkTextSize = discreteSeekBar.sp2px(sp)
        return this
    }

    fun setTickMarkTextColor(@ColorRes colorRes: Int): ConfigBuilder {
        this.tickMarkTextColor = ContextCompat.getColor(discreteSeekBar.context, colorRes)
        return this
    }

    fun setTickMarkDrawable(drawable: Drawable): ConfigBuilder {
        this.tickMarkDrawable = drawable
        return this
    }

    fun setTickMarkTextArray(tickMarkTextArray: SparseArray<String>): ConfigBuilder {
        this.tickMarkTextArray = tickMarkTextArray
        return this
    }

    fun setThumbDefaultSize(diameterDp: Int): ConfigBuilder {
        this.thumbDefaultRadius = discreteSeekBar.dp2px(diameterDp) / 2
        return this
    }

    fun setThumbPressedSize(diameterDp: Int): ConfigBuilder {
        this.thumbPressedRadius = discreteSeekBar.dp2px(diameterDp) / 2
        return this
    }

    fun setThumbColor(@ColorRes colorRes: Int): ConfigBuilder {
        this.thumbColor = ContextCompat.getColor(discreteSeekBar.context, colorRes)
        return this
    }

    fun setOnValueChangedListener(onValueChangedListener: DiscreteSeekBar.OnValueChangedListener): ConfigBuilder {
        this.onValueChangedListener = onValueChangedListener
        return this
    }

    fun setTrackTouchEnable(trackTouchEnable: Boolean): ConfigBuilder {
        this.trackTouchEnable = trackTouchEnable
        return this
    }

    fun setTouchAreaFactor(factor: Float): ConfigBuilder {
        this.touchAreaFactor = factor
        return this
    }

    fun setAnimDuration(duration: Long): ConfigBuilder {
        this.animDuration = duration
        return this
    }
}