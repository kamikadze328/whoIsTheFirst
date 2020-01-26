package com.kamikadze328.whoisthefirst.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.activities.MultiTouchActivity
import com.kamikadze328.whoisthefirst.auxiliary_classes.Coordinates
import com.kamikadze328.whoisthefirst.auxiliary_classes.CustomCountDownTimer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class MultiTouchCustomView(context: Context, attributeSet: AttributeSet):View(context, attributeSet) {
    val COLORS = intArrayOf(
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.CYAN
    )

    private var scheduledService: ScheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null

    private var coordinates: ArrayList<Coordinates> = ArrayList()
    private val paint: Paint = Paint()
    private val activity: MultiTouchActivity = context as MultiTouchActivity
    private var lastPointersCount = 0
    private var textTimer: CustomCountDownTimer? = null
    var youAreAlone = false


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawHelpText()
            if (lastPointersCount != 0) { drawTouches(canvas, coordinates, paint) }
        }
    }

    private fun drawTouches(
        canvas: Canvas,
        coordinates: List<Coordinates>,
        paint: Paint
    ) {
        canvas.drawColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

        for (i in coordinates.indices) {
            val current = coordinates[i]
            val x: Float = current.x
            val y: Float = current.y
            paint.color = COLORS[i % 5]

            // Set the width of the circle.
            paint.strokeWidth = width / 40f
            paint.style = Paint.Style.STROKE
            paint.isAntiAlias = true
            canvas.drawCircle(x, y, width / 7.7f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        coordinates = ArrayList()
        val pointerCount = event.pointerCount

        // For each of the pointers of touches -> Put their coordinates to draw them in onDraw().
        for (i in 0 until pointerCount) {
            val x = event.getX(i)
            val y = event.getY(i)
            coordinates.add(Coordinates(x, y))
        }

        if (lastPointersCount != pointerCount) {
            youAreAlone = false
            startScheduleToRandom(pointerCount)

            Log.d("TIMEKEK", "( $lastPointersCount -> $pointerCount )")
        }
        lastPointersCount = pointerCount

        this.invalidate()
        var toReturn = true

        // If the last touch pointer is removed -> remove its circle.
        if (event.action == MotionEvent.ACTION_UP) {
            youAreAlone = false
            Log.d("TIMEKEK", "( $lastPointersCount -> 0 )")
            try {
                coordinates.removeAt(0)
            } catch (e: Exception) {
            }
            checkAndStopScheduleAndTextTimer()
            lastPointersCount--

            toReturn = false
        }
        return toReturn
    }

    private fun startScheduleToRandom(countTouches: Int) {
        checkAndStopScheduleAndTextTimer()
        if (countTouches == 1) {
            future = scheduledService.schedule(
                {
                    Log.d("TIMEKEK", "расчёт окончен!")
                    youAreAlone = true
                },
                1600,
                TimeUnit.MILLISECONDS
            )
        } else {

            future = scheduledService.schedule(
                {
                    Log.d("TIMEKEK", "расчёт окончен!")
                },
                2,
                TimeUnit.SECONDS
            )
            startTextTimer()
        }
    }

    private fun isScheduleNotDone(): Boolean = future != null && !future!!.isDone

    private fun checkAndStopScheduleAndTextTimer() {

        if (isScheduleNotDone()) {
            future!!.cancel(false)
            Log.d("TIMEKEK", "отмена!")
            textTimer?.cancel()
        }
    }

    private fun startTextTimer() {
        textTimer = CustomCountDownTimer(
            2000, 10, activity.findViewById(R.id.helpTextView), width
        )
            .start() as CustomCountDownTimer
    }

    private fun drawHelpText() {
        val helpTextView = activity.findViewById<TextView>(R.id.helpTextView)
        if (lastPointersCount == 0 || (lastPointersCount == 1 && !youAreAlone)) {
            helpTextView.textSize = width / 54f
            when (MultiTouchActivity.mode) {
                "1" -> helpTextView.text = resources.getString(R.string.helpWhoIsFirst)
                "123" -> helpTextView.text = resources.getString(R.string.helpQueue)
            }
        } else if (lastPointersCount == 1 && youAreAlone) {
            helpTextView.textSize = width / 54f
            helpTextView.text = resources.getString(R.string.youAreOnlyTheFirst)
        }
    }
}



