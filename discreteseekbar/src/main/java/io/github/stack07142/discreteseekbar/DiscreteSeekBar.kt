package io.github.stack07142.discreteseekbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.round

/**
 *
 * Kotlin
 * companion object 상수
 */
class DiscreteSeekBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    // value
    private var minValue: Int
    private var maxValue: Int
    private var unitValue: Int
    private var sectionCount: Int
    private var value: Int = 0
    private var valueIndex: Int
        get() = (value - minValue) / unitValue

    // track
    private var trackWidth: Int
    private var trackColor: Int
    private var trackLength: Float = 0f
    private var trackSectionLength: Float = 0f

    // tickMark
    private var tickMarkTextTopMargin: Int
    private var tickMarkTextSize: Int
    private var tickMarkTextColor: Int
    private var tickMarkDrawable: Drawable?
    private var tickMarkTextArray = SparseArray<String>()

    // thumb
    private var thumbDefaultRadius: Int
    private var thumbPressedRadius: Int
    private var thumbColor: Int

    // coordinate
    private var thumbCenterX: Float = 0f
    private var thumbPreCenterX: Float = 0f
    private var trackStartX: Float = 0f
    private var trackEndX: Float = 0f

    // touch event
    private var trackTouchEnable: Boolean
    private var onValueChangedListener: OnValueChangedListener? = null
    private var isThumbDragging: Boolean = false
    private var touchAreaFactor: Float = 1.7f
    private var animDuration: Long = 300L

    // Paint
    private val paint: Paint
    private val tickMarkTextRect: Rect

    // Builder
    private var configBuilder: ConfigBuilder? = null

    /*
     * Constructor
     */
    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiscreteSeekBar, defStyleAttr, 0)

        // value
        this.maxValue = typedArray.getInteger(R.styleable.DiscreteSeekBar_attr_maxValue, 0)
        this.minValue = typedArray.getInteger(R.styleable.DiscreteSeekBar_attr_minValue, 100)
        this.sectionCount = typedArray.getInteger(R.styleable.DiscreteSeekBar_attr_sectionCount, 2)
        this.unitValue = (maxValue - minValue) / sectionCount
        this.value = typedArray.getInteger(R.styleable.DiscreteSeekBar_attr_value, 0)
        this.valueIndex = (value - minValue) / unitValue

        // track
        this.trackWidth = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_attr_trackWidth, dp2px(1))
        this.trackColor = typedArray.getColor(R.styleable.DiscreteSeekBar_attr_trackColor, ContextCompat.getColor(context, R.color.colorGray))

        // tickMark
        this.tickMarkTextTopMargin = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_attr_tickMarkTopMargin, dp2px(19))
        this.tickMarkTextSize = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_attr_tickMarkTextSize, sp2px(12))
        this.tickMarkTextColor = typedArray.getColor(R.styleable.DiscreteSeekBar_attr_tickMarkTextColor, ContextCompat.getColor(context, R.color.colorBlack))
        this.tickMarkDrawable = typedArray.getDrawable(R.styleable.DiscreteSeekBar_attr_tickMarkDrawable)

        // thumb
        this.thumbDefaultRadius = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_attr_thumbDefaultSize, dp2px(16)) / 2
        this.thumbPressedRadius = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_attr_thumbPressedSize, dp2px(24)) / 2
        this.thumbColor = typedArray.getColor(R.styleable.DiscreteSeekBar_attr_thumbColor, ContextCompat.getColor(context, R.color.colorRed100))

        // touch event
        this.trackTouchEnable = typedArray.getBoolean(R.styleable.DiscreteSeekBar_attr_trackTouchEnable, true)

        typedArray.recycle()

        paint = Paint()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND

        tickMarkTextRect = Rect()
    }

    /*
     * Measure & Draw
     */

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        trackStartX = round((paddingLeft + thumbPressedRadius).toFloat())
        trackEndX = round((measuredWidth - paddingRight - thumbPressedRadius).toFloat())

        trackSectionLength = round((trackEndX - trackStartX) / sectionCount)
        trackEndX = trackStartX + sectionCount * trackSectionLength

        trackLength = trackEndX - trackStartX

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        val layoutHeight = thumbPressedRadius + tickMarkTextTopMargin + sp2px(tickMarkTextSize) * 2

        setMeasuredDimension(measuredWidth, if (heightMode == View.MeasureSpec.EXACTLY) measuredHeight else layoutHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 1. Draw track
        canvas.translate(trackStartX, (measuredHeight / 2).toFloat())
        var initCount = canvas.save()

        paint.color = trackColor
        paint.strokeWidth = trackWidth.toFloat()
        canvas.drawLine(0f, 0f, trackLength, 0f, paint)

        canvas.restoreToCount(initCount)

        initCount = canvas.save()

        paint.textAlign = Paint.Align.CENTER
        paint.color = tickMarkTextColor
        paint.textSize = tickMarkTextSize.toFloat()

        // 2. Draw tickMarks
        val tickMarkWidth = tickMarkDrawable?.intrinsicWidth
        val tickMarkHeight = tickMarkDrawable?.intrinsicHeight
        val tickMarkHalfWidth = tickMarkWidth?.div(2)
        val tickMarkHalfHeight = tickMarkHeight?.div(2)

        tickMarkDrawable?.setBounds(tickMarkHalfWidth?.unaryMinus()
                ?: 0, tickMarkHalfHeight?.unaryMinus() ?: 0, tickMarkHalfWidth
                ?: 0, tickMarkHalfHeight ?: 0)

        // 3. Draw tickMarkText
        var i = minValue
        while (i <= maxValue) {

            if (i != value) {
                tickMarkDrawable?.draw(canvas)
            }

            if (tickMarkTextArray.get(i, null) != null) {

                val count = canvas.save()
                canvas.translate(0f, tickMarkTextTopMargin.toFloat())
                paint.getTextBounds(tickMarkTextArray.get(i), 0, tickMarkTextArray.get(i).length, tickMarkTextRect)

                var textX = 0
                if (i == minValue) {
                    textX = tickMarkTextRect.width() / 2
                } else if (i == maxValue) {
                    textX = -(tickMarkTextRect.width() / 2)
                }

                canvas.drawText(tickMarkTextArray.get(i), textX.toFloat(), tickMarkTextRect.height().toFloat(), paint)
                canvas.restoreToCount(count)
            }

            canvas.translate(trackSectionLength, 0f)
            i += unitValue
        }

        canvas.restoreToCount(initCount)

        // 4. Draw thumb
        if (!isThumbDragging) {
            thumbCenterX = trackStartX + trackSectionLength * valueIndex
        }

        paint.color = thumbColor
        canvas.drawCircle(thumbCenterX - trackStartX, 0f, (if (isThumbDragging) thumbPressedRadius else thumbDefaultRadius).toFloat(), paint)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                parent.requestDisallowInterceptTouchEvent(true)

                isThumbDragging = detectThumbIsTouched(event)

                if (isThumbDragging) {
                    invalidate()
                } else if (trackTouchEnable && detectTrackIsTouched(event)) {
                    isThumbDragging = true

                    val x = getThumbCenterX(event.x)
                    if (x != thumbPreCenterX) {
                        thumbPreCenterX = x
                        thumbCenterX = thumbPreCenterX
                        updateValueWithThumbCenterX()

                        invalidate()
                        notifyValueChanged(value)
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isThumbDragging) {
                    val x = getThumbCenterX(event.x)

                    if (x != thumbPreCenterX) {
                        thumbPreCenterX = x
                        thumbCenterX = thumbPreCenterX
                        updateValueWithThumbCenterX()

                        invalidate()
                        notifyValueChanged(value)
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)

                if (isThumbDragging) {
                    animate()
                            .setDuration(animDuration)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationCancel(animation: Animator) {
                                    isThumbDragging = false
                                    invalidate()
                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    isThumbDragging = false
                                    invalidate()
                                }
                            }).start()
                }
            }
        }
        return isThumbDragging || super.onTouchEvent(event)
    }

    /*
     * Detect & Calculate
     */

    private fun detectThumbIsTouched(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        val thumbX = trackStartX + trackSectionLength * valueIndex
        val thumbY = (measuredHeight / 2).toFloat()

        val diffX = event.x - thumbX
        val diffY = event.y - thumbY

        val thumbTouchRadius = thumbPressedRadius * touchAreaFactor

        return squareOf(diffX) + squareOf(diffY) <= squareOf(thumbTouchRadius)
    }

    private fun detectTrackIsTouched(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        val touchAreaStartX = trackStartX - thumbPressedRadius
        val touchAreaEndX = trackEndX + thumbPressedRadius

        val touchAreaStartY = paddingTop.toFloat()
        val touchAreaEndY = (measuredHeight - paddingBottom).toFloat()

        return ((event.x in touchAreaStartX..touchAreaEndX)
                && (event.y in touchAreaStartY..touchAreaEndY))
    }

    private fun getThumbCenterX(touchedX: Float): Float {
        if (touchedX <= trackStartX) return trackStartX
        if (touchedX >= trackEndX) return trackEndX

        var i = 0
        var x = 0f
        while (i <= sectionCount) {
            x = trackStartX + i * trackSectionLength
            if (x <= touchedX && touchedX - x <= trackSectionLength) {
                break
            }
            i++
        }

        return if (touchedX - x <= trackSectionLength / 2f) {
            x
        } else {
            x + trackSectionLength
        }
    }

    private fun updateValueWithThumbCenterX() {
        value = minValue + ((thumbCenterX - trackStartX) / trackSectionLength).toInt() * unitValue
    }

    /*
     * Events
     */

    interface OnValueChangedListener {
        fun onValueChanged(value: Int)
    }

    private fun notifyValueChanged(value: Int) {
        this.onValueChangedListener?.onValueChanged(value)
    }

    /*
     * Utils
     */

    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    fun sp2px(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    private fun squareOf(f: Float): Float {
        return f * f
    }

    /*
     * Builder
     */

    fun setValue(value: Int) {
        this.value = value

        postInvalidate()
    }

    fun getConfigBuilder(): ConfigBuilder {
        if (configBuilder == null) {
            configBuilder = ConfigBuilder(this)
        }

        configBuilder?.minValue = this.minValue
        configBuilder?.maxValue = this.maxValue
        configBuilder?.sectionCount = this.sectionCount
        configBuilder?.value = this.value

        configBuilder?.trackWidth = this.trackWidth
        configBuilder?.trackColor = this.trackColor

        configBuilder?.tickMarkTextTopMargin = this.tickMarkTextTopMargin
        configBuilder?.tickMarkTextSize = this.tickMarkTextSize
        configBuilder?.tickMarkTextColor = this.tickMarkTextColor
        configBuilder?.tickMarkDrawable = this.tickMarkDrawable
        configBuilder?.tickMarkTextArray = this.tickMarkTextArray

        configBuilder?.thumbDefaultRadius = this.thumbDefaultRadius
        configBuilder?.thumbPressedRadius = this.thumbPressedRadius
        configBuilder?.thumbColor = this.thumbColor

        configBuilder?.trackTouchEnable = this.trackTouchEnable
        configBuilder?.onValueChangedListener = this.onValueChangedListener
        configBuilder?.touchAreaFactor = this.touchAreaFactor
        configBuilder?.animDuration = this.animDuration

        return configBuilder ?: ConfigBuilder(this)
    }

    fun config(configBuilder: ConfigBuilder) {

        this.minValue = configBuilder.minValue
        this.maxValue = configBuilder.maxValue
        this.sectionCount = configBuilder.sectionCount
        this.value = configBuilder.value
        this.unitValue = (maxValue - minValue) / sectionCount
        this.valueIndex = (value - minValue) / unitValue

        this.trackWidth = configBuilder.trackWidth
        this.trackColor = configBuilder.trackColor

        this.tickMarkTextTopMargin = configBuilder.tickMarkTextTopMargin
        this.tickMarkTextSize = configBuilder.tickMarkTextSize
        this.tickMarkTextColor = configBuilder.tickMarkTextColor
        this.tickMarkDrawable = configBuilder.tickMarkDrawable
        this.tickMarkTextArray = configBuilder.tickMarkTextArray

        this.thumbDefaultRadius = configBuilder.thumbDefaultRadius
        this.thumbPressedRadius = configBuilder.thumbPressedRadius
        this.thumbColor = configBuilder.thumbColor

        this.trackTouchEnable = configBuilder.trackTouchEnable
        this.onValueChangedListener = configBuilder.onValueChangedListener
        this.touchAreaFactor = configBuilder.touchAreaFactor
        this.animDuration = configBuilder.animDuration

        requestLayout()
    }
}