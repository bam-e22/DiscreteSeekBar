package io.github.stack07142.kotlindiscreteslider

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
import android.util.Log
import android.util.SparseArray
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import io.github.stack07142.discreteseekbar.R
import kotlin.math.round

/**
 * 1. 빌더
 * 2. TickMarkText 자동 생성 기능 추가
 * value float으로
 *
 * Kotlin
 * !!
 * companion object 상수
 * get() 함수
 */
class DiscreteSeekBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    // value
    private val minValue: Int
    private val maxValue: Int
    private val unitValue: Int
    private val sectionCount: Int
    private var value: Int = 0
    private val valueIndex: Int
        get() = (value - minValue) / unitValue

    // track
    private val trackWidth: Int
    private val trackColor: Int
    private var trackLength: Float = 0f
    private var trackSectionLength: Float = 0f

    // tickMark
    private var tickMarkTextArray = SparseArray<String>()
    private val tickMarkDrawable: Drawable?
    private val tickMarkTextTopMargin: Int
    private val tickMarkTextSize: Int
    private val tickMarkTextColor: Int

    // thumb
    private val thumbDefaultRadius: Int
    private val thumbPressedRadius: Int
    private val thumbColor: Int

    // coordinate
    private var thumbCenterX: Float = 0f
    private var thumbPreCenterX: Float = 0f
    private var trackStartX: Float = 0f
    private var trackEndX: Float = 0f

    // touch event
    private var isThumbDragging: Boolean = false
    private var onValueChangedListener: OnValueChangedListener? = null
    private val trackTouchEnable: Boolean

    // Paint
    private val paint: Paint
    private val tickMarkTextRect: Rect

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiscreteSeekBar, defStyleAttr, 0)

        // value
        this.maxValue = typedArray.getInteger(R.styleable.DiscreteSeekBar_DiscreteSeekBar_maxValue, 0)
        this.minValue = typedArray.getInteger(R.styleable.DiscreteSeekBar_DiscreteSeekBar_minValue, 0)
        this.sectionCount = typedArray.getInteger(R.styleable.DiscreteSeekBar_DiscreteSeekBar_sectionCount, 0)
        this.unitValue = (maxValue - minValue) / sectionCount
        this.value = typedArray.getInteger(R.styleable.DiscreteSeekBar_DiscreteSeekBar_value, 0)

        // track
        this.trackWidth = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_DiscreteSeekBar_trackWidth, dp2px(1))
        this.trackColor = typedArray.getColor(R.styleable.DiscreteSeekBar_DiscreteSeekBar_trackColor, ContextCompat.getColor(context, R.color.colorGray))

        // tickMark
        this.tickMarkTextSize = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_DiscreteSeekBar_tickMarkTextSize, sp2px(12))
        this.tickMarkTextColor = typedArray.getColor(R.styleable.DiscreteSeekBar_DiscreteSeekBar_tickMarkTextColor, ContextCompat.getColor(context, R.color.colorBlack))
        this.tickMarkTextTopMargin = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_DiscreteSeekBar_tickMarkTopMargin, dp2px(19))
        this.tickMarkDrawable = typedArray.getDrawable(R.styleable.DiscreteSeekBar_DiscreteSeekBar_tickMarkDrawable)

        // thumb
        this.thumbDefaultRadius = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_DiscreteSeekBar_thumbDefaultSize, dp2px(16)) / 2
        this.thumbPressedRadius = typedArray.getDimensionPixelSize(R.styleable.DiscreteSeekBar_DiscreteSeekBar_thumbPressedSize, dp2px(24)) / 2
        this.thumbColor = typedArray.getColor(R.styleable.DiscreteSeekBar_DiscreteSeekBar_thumbColor, ContextCompat.getColor(context, R.color.colorRed100))

        // touch event
        this.trackTouchEnable = typedArray.getBoolean(R.styleable.DiscreteSeekBar_DiscreteSeekBar_trackTouchEnable, false)

        typedArray.recycle()

        paint = Paint()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND

        tickMarkTextRect = Rect()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        trackStartX = round((paddingLeft + thumbPressedRadius).toFloat())
        trackEndX = round((measuredWidth - paddingRight - thumbPressedRadius).toFloat())
        trackLength = trackEndX - trackStartX
        trackSectionLength = round(trackLength / sectionCount)

        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        val layoutHeight = thumbPressedRadius + tickMarkTextTopMargin + sp2px(tickMarkTextSize)

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
        if (tickMarkDrawable != null) {
            val tickMarkWidth = tickMarkDrawable.intrinsicWidth
            val tickMarkHeight = tickMarkDrawable.intrinsicHeight
            val tickMarkHalfWidth = if (tickMarkWidth >= 0) tickMarkWidth / 2 else 1
            val tickMarkHalfHeight = if (tickMarkHeight >= 0) tickMarkHeight / 2 else 1

            tickMarkDrawable.setBounds(-tickMarkHalfWidth, -tickMarkHalfHeight, tickMarkHalfWidth, tickMarkHalfHeight)
        }

        // 3. Draw tickMarkText
        var i = minValue
        while (i <= maxValue) {

            if (i != value && tickMarkDrawable != null) {
                tickMarkDrawable.draw(canvas)
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
            thumbCenterX = round(trackStartX + trackSectionLength * valueIndex)
        }

        Log.d("todd", "onDraw() - isThumbDragging=$isThumbDragging, thumbCenterX=$thumbCenterX")
        paint.color = thumbColor
        canvas.drawCircle(thumbCenterX - trackStartX, 0f, (if (isThumbDragging) thumbPressedRadius else thumbDefaultRadius).toFloat(), paint)
    }

    fun setValue(value: Int) {
        this.value = value

        postInvalidate()
    }

    fun setTickMarkTextArray(tickMarkTextArray: SparseArray<String>) {
        this.tickMarkTextArray = tickMarkTextArray

        requestLayout()
        invalidate()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("todd", "@@@@@@@ACTION_DOWN")
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
                Log.d("todd", "@@@@@@@ACTION_MOVE, isThumbDragging=$isThumbDragging")
                if (isThumbDragging) {
                    val x = getThumbCenterX(event.x)

                    Log.d("todd", "\n-------------------\n")
                    Log.d("todd", "x = $x")
                    Log.d("todd", "thumbPreCenterX = $thumbPreCenterX")
                    Log.d("todd", "thumbCenterX = $thumbCenterX")
                    Log.d("todd", "\n-------------------\n")

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
                Log.d("todd", "@@@@@@@ACTION_UP")
                parent.requestDisallowInterceptTouchEvent(false)

                if (isThumbDragging) {
                    animate()
                            .setDuration(ANIM_DURATION)
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

    private fun detectThumbIsTouched(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        val thumbX = trackStartX + trackSectionLength * valueIndex
        val thumbY = (measuredHeight / 2).toFloat()

        val diffX = event.x - thumbX
        val diffY = event.y - thumbY

        val thumbTouchRadius = thumbPressedRadius * TOUCH_AREA_FACTOR

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
            Log.d("todd", "getThumbCenterX, x = $x")
            round(x)
        } else {
            Log.d("todd", "getThumbCenterX, x = " + (x + trackSectionLength))
            round(x + trackSectionLength)
        }
    }

    private fun updateValueWithThumbCenterX() {
        value = minValue + ((thumbCenterX - trackStartX) / trackSectionLength).toInt() * unitValue
        Log.d("todd", "updateValueWithThumbCenterX, value = $value")
    }

    interface OnValueChangedListener {
        fun onValueChanged(value: Int)
    }

    fun setOnValueChangedListener(onValueChangedListener: OnValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener
    }

    private fun notifyValueChanged(value: Int) {
        if (this.onValueChangedListener != null) {
            this.onValueChangedListener!!.onValueChanged(value)
        }
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    private fun sp2px(sp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(),
                Resources.getSystem().displayMetrics).toInt()
    }

    private fun squareOf(f: Float): Float {
        return f * f
    }

    companion object {
        private const val TOUCH_AREA_FACTOR = 1.7f
        private const val ANIM_DURATION = 300L
    }
}