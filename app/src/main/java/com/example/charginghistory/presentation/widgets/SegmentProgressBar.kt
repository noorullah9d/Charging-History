package com.example.charginghistory.presentation.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.charginghistory.R

class SegmentProgressBar : View {
    private var progressWidth = 0F
    private var maxProgress = 0F

    private var cornerRadius = DEFAULT_CORNERS.toFloat()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressRectF = RectF()
    private val path = Path()

    private var primaryProgressPercent = 0F
    private var secondaryProgressPercent = 0F

    private var backgroundColor = 0
    private var primaryColor = 0
    private var secondaryColor = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val t = context.obtainStyledAttributes(attrs, R.styleable.SegmentProgressBar, 0, 0)

        backgroundColor =
            t.getColor(R.styleable.SegmentProgressBar_bgColor, PROGRESS_BAR_BG_COLOR)
        primaryColor =
            t.getColor(R.styleable.SegmentProgressBar_primaryColor, DEFAULT_PRIMARY_COLOR)
        secondaryColor =
            t.getColor(R.styleable.SegmentProgressBar_secondaryColor, DEFAULT_SECONDARY_COLOR)

        primaryProgressPercent =
            t.getFloat(R.styleable.SegmentProgressBar_primaryPercent, 0.0F)
        secondaryProgressPercent =
            t.getFloat(R.styleable.SegmentProgressBar_secondaryPercent, 0.0F)

        cornerRadius =
            t.getDimensionPixelSize(R.styleable.SegmentProgressBar_cornerRadius, DEFAULT_CORNERS)
                .toFloat()

        maxProgress =
            t.getFloat(R.styleable.SegmentProgressBar_maxProgress, DEFAULT_MAX_PROGRESS)

        t.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setMaxWidth()
    }

    private fun setMaxWidth() {
        progressWidth = width.toFloat()
        invalidate()
    }

    fun setMax(max: Float) {
        maxProgress = max
    }

    fun setPrimaryProgress(value: Float) {
        primaryProgressPercent = value
        invalidate()
    }

    fun setSecondaryProgress(value: Float) {
        secondaryProgressPercent = value
        invalidate()
    }

    fun setCornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = backgroundColor
        drawRoundRect(
            canvas,
            0F,
            0F,
            progressWidth,
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            paint
        )

        paint.color = primaryColor
        val primaryRight = (progressWidth / maxProgress) * primaryProgressPercent
        drawRoundRect(
            canvas,
            0F,
            0F,
            primaryRight,
            height.toFloat(),
            cornerRadius,
            O,
            cornerRadius,
            O,
            paint
        )

        paint.color = secondaryColor
        val secondaryRight = (progressWidth / maxProgress) * secondaryProgressPercent
        drawRoundRect(
            canvas,
            primaryRight,
            O,
            primaryRight + secondaryRight,
            height.toFloat(),
            O,
            cornerRadius,
            O,
            cornerRadius,
            paint
        )
    }

    private fun drawRoundRect(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        topLeftRadius: Float,
        topRightRadius: Float,
        bottomLeftRadius: Float,
        bottomRightRadius: Float,
        paint: Paint
    ) {
        val radiusArr = floatArrayOf(
            topLeftRadius, topLeftRadius,
            topRightRadius, topRightRadius,
            bottomRightRadius, bottomRightRadius,
            bottomLeftRadius, bottomLeftRadius
        )

        path.reset()
        progressRectF.set(left, top, right, bottom)
        path.addRoundRect(progressRectF, radiusArr, Path.Direction.CW)
        path.close()
        canvas.drawPath(path, paint)
    }

    companion object {
        const val O = 0F
        const val DEFAULT_MAX_PROGRESS = 100.0F
        const val DEFAULT_CORNERS = 6

        private val PROGRESS_BAR_BG_COLOR = Color.parseColor("#333333")
        private val DEFAULT_PRIMARY_COLOR = Color.parseColor("#ffbfbf")
        private val DEFAULT_SECONDARY_COLOR = Color.parseColor("#a2fab3")
    }
}