package com.kamikadze328.whoisthefirst.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.auxiliary_classes.Pointer
import com.kamikadze328.whoisthefirst.data.DoubleTapObservable
import com.kamikadze328.whoisthefirst.data.TextSize

class MultiTouchCustomView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet), DoubleTapObservable {
    override val listeners: MutableSet<() -> Boolean> = mutableSetOf()

    private val helpText: TextView by lazy { findViewById(R.id.helpTextView) }

    private var colors: MutableList<Int> = mutableListOf()

    private var pointers: List<Pointer> = emptyList()

    private val paint: Paint by lazy { Paint() }

    private var circleRadius = 0f
    private var circleShadowRadius = 0f
    private var circleStrokeWidth = 0f
    private var animationOffset = 0
    private var numberSizeInsideCircle = 0f

    private var isCirclesWithNumbers = false

    init {
        init(attributeSet)

        resources.getIntArray(R.array.circle_colors).forEach { colors.add(it) }
        colors.shuffle()
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.custom_view_multitouch, this)

        context.obtainStyledAttributes(attrs, R.styleable.MultiTouchCustomView).apply {
            val text = getString(R.styleable.MultiTouchCustomView_multitouch_text) ?: ""
            val isTextBig = getBoolean(R.styleable.MultiTouchCustomView_multitouch_isTextBig, false)
            recycle()

            setText(text, if (isTextBig) TextSize.BIG else TextSize.NORMAL)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        calculateValues()
    }

    private fun calculateValues() {
        circleRadius = (height / 2) / 7.7f
        circleStrokeWidth = width / 50f
        circleShadowRadius = width / 25f

        numberSizeInsideCircle = width / 6f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        drawTouches(canvas)

    }

    fun drawTouches(pointers: List<Pointer>) {
        isCirclesWithNumbers = false
        this.pointers = pointers
        invalidate()
    }

    fun drawTouchesWithCircles(pointers: List<Pointer>) {
        isCirclesWithNumbers = true
        this.pointers = pointers
        invalidate()
    }

    private fun drawTouches(canvas: Canvas) {
        if (isCirclesWithNumbers)
            pointers.forEach { drawOneFromQueue(canvas, it) }
        else
            pointers.forEach { drawCircle(canvas, it) }

    }

    private fun drawCircle(canvas: Canvas, p: Pointer) {
        val color = colors[p.id % colors.size]
        paint.color = color
        paint.strokeWidth = circleStrokeWidth
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.setShadowLayer(circleShadowRadius, 0f, 0f, color)

        canvas.drawCircle(p.x, p.y, circleRadius, paint)
    }

    private fun drawOneFromQueue(canvas: Canvas, p: Pointer) {
        drawCircle(canvas, p)

        paint.strokeWidth = width / 150f
        paint.textSize = width / 20f
        val vOffset = -width / 50f

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.setShadowLayer(0f, 0f, 0f, 0)

        //Numbers around a Circle
        val path = Path()
        val placeInLine = (p.placeInQueue + 1).toString()
        path.addCircle(p.x, p.y, circleRadius, Path.Direction.CW)
        val circumferenceCircle = circleRadius * 2 * Math.PI.toFloat()
        for (i in -3..3) {
            var hOffset = circumferenceCircle * i / 7 + animationOffset
            hOffset =
                if (hOffset > circumferenceCircle / 2) (hOffset - circumferenceCircle) else hOffset

            canvas.drawTextOnPath(
                placeInLine,
                path,
                hOffset,
                vOffset,
                paint
            )
        }

        //Number inside a circle
        paint.textSize = numberSizeInsideCircle
        canvas.drawText(placeInLine, p.x, p.y + width / 17f, paint)
    }


    fun setText(resId: Int, textSize: TextSize = TextSize.NORMAL) {
        setText(context.getString(resId), textSize)
    }

    fun setTextSize(dimenRes: Int) {
        helpText.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(dimenRes)
        )
        helpText.gravity = Gravity.CENTER
    }

    fun setText(text: String, textSize: TextSize = TextSize.NORMAL) {
        helpText.text = text
        setTextSize(textSize.dimenId)
    }
}
