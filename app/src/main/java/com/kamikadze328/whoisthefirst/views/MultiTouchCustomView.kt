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
import com.kamikadze328.whoisthefirst.auxiliary_classes.CustomCountDownTimer
import com.kamikadze328.whoisthefirst.auxiliary_classes.Pointer
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
    private val activity: MultiTouchActivity = context as MultiTouchActivity

    private var scheduledService: ScheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor()
    private var future: ScheduledFuture<*>? = null
    private var textTimer: CustomCountDownTimer? = null

    private var coordinates: ArrayList<Pointer> = ArrayList()
    private val paint: Paint = Paint()

    private var lastPointersCount = 0
    private var areYouAlone = false
    private var isTimerSuccessEnded = false
    private var winnerID: Int = -1
    private var winnerIndex: Int = -1




    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawHelpText()
            if (lastPointersCount != 0) {
                drawTouches(canvas, coordinates)
            }
        }
    }

    private fun drawTouches(
        canvas: Canvas,
        coordinates: List<Pointer>
    ) {
        canvas.drawColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

        for (i in coordinates.indices) {
            if(winnerID<0) {
                drawCircle(canvas, i)
            }
            else if (coordinates[i].id == winnerID){
                drawCircle(canvas, i)
                break
            }
        }
    }
    private fun drawCircle(canvas: Canvas, i:Int){
        val current = coordinates[i]
        val x: Float = current.x
        val y: Float = current.y
        val id: Int = current.id
        paint.color = COLORS[id % 5]

        // Set the width of the circle.
        paint.strokeWidth = width / 40f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        // Set the width of the lines.

        canvas.drawCircle(x, y, width / 7.7f, paint)
        // Draw the number of the specific circle next to it in white colour.
        paint.color = Color.WHITE
        paint.strokeWidth = 5f
        paint.style = Paint.Style.FILL
        // Set the size for the numbers.
        // Set the size for the numbers.
        paint.textSize = 75f
        canvas.drawText((id).toString(), x + 110, y, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        coordinates = ArrayList()
        val pointerCount = event.pointerCount
        // For each of the pointers of touches -> Put their coordinates to draw them in onDraw().
        for (i in 0 until pointerCount) {
            if(winnerIndex<0 || !isTimerSuccessEnded) {
                coordinates.add(Pointer(event.getX(i), event.getY(i), event.getPointerId(i)))
            } else if(winnerID>=0 && winnerID==event.getPointerId(i)){
                Log.d("KEK", "winnerID($winnerID)>=0")
                coordinates.clear()
                coordinates.add(Pointer(event.getX(i), event.getY(i), winnerID))
                break
            } else if (i == winnerIndex) {
                winnerID = event.getPointerId(winnerIndex)
                coordinates.clear()
                coordinates.add(Pointer(event.getX(i), event.getY(i), winnerID))
                Log.d("EVENT", "Нашёлся победитель id ---- $winnerID")
                break
            }

        }

        if (lastPointersCount != pointerCount) {
            areYouAlone = false
            if(!isTimerSuccessEnded) startScheduleToRandom(pointerCount)
        }
        lastPointersCount = pointerCount

        this.invalidate()

        // If the last touch pointer is removed -> remove its circle.
        if (event.action == MotionEvent.ACTION_UP) {
            ahShitHereWeGoAgain()
            return false
        }
        return true
    }

    private fun startScheduleToRandom(countTouches: Int) {
        checkAndStopScheduleAndTextTimer()
        if (countTouches == 1) {
            future = scheduledService.schedule(
                {
                    Log.d("TIMEKEK", "расчёт окончен!")
                    areYouAlone = true
                },
                1600,
                TimeUnit.MILLISECONDS
            )
        } else {

            future = scheduledService.schedule(
                {
                    Log.d("TIMEKEK", "расчёт окончен!")
                    isTimerSuccessEnded = true
                    winnerIndex = getOneRandomPointer()
                    Log.d("WINNERINDEX", "$winnerIndex")
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
        if (lastPointersCount == 0 || (lastPointersCount == 1 && !areYouAlone)) {
            helpTextView.textSize = width / 54f
            when (MultiTouchActivity.mode) {
                "1" -> {
                    helpTextView.text = if(isTimerSuccessEnded) resources.getString(R.string.helpStartAgain)
                    else resources.getString(R.string.helpWhoIsFirst)
                }
                "123" -> helpTextView.text = resources.getString(R.string.helpQueue)
            }
        } else if (lastPointersCount == 1 && areYouAlone) {
            helpTextView.textSize = width / 54f
            helpTextView.text = resources.getString(R.string.youAreOnlyTheFirst)
        }

    }

    private fun getOneRandomPointer(): Int = (0 until lastPointersCount).random()
    private fun ahShitHereWeGoAgain() {
        winnerIndex = -1
        winnerID = -1
        isTimerSuccessEnded = false
        areYouAlone = false
        lastPointersCount--
        checkAndStopScheduleAndTextTimer()
        try {
            coordinates.removeAt(0)
        } catch (e: Exception) {
        }
    }

}



