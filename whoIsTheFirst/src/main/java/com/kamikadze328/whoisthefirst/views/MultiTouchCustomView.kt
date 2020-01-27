package com.kamikadze328.whoisthefirst.views

import android.content.Context
import android.graphics.Canvas
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
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MultiTouchCustomView(context: Context, attributeSet: AttributeSet):View(context, attributeSet) {
    private val colors: MutableList<Int> = mutableListOf(
        ContextCompat.getColor(context, R.color.colorCircleRed),
        ContextCompat.getColor(context, R.color.colorCirclePink),
        ContextCompat.getColor(context, R.color.colorCirclePurple),
        ContextCompat.getColor(context, R.color.colorCircleBlue),
        ContextCompat.getColor(context, R.color.colorCircleLightBlue),
        ContextCompat.getColor(context, R.color.colorCircleMint),
        ContextCompat.getColor(context, R.color.colorCircleAppleGreen),
        ContextCompat.getColor(context, R.color.colorCircleYellow),
        ContextCompat.getColor(context, R.color.colorCircleOrange),
        ContextCompat.getColor(context, R.color.colorCircleWhite)
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

    init{
        colors.shuffle()
        colors.shuffle()
        colors.shuffle()
    }

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
        val x = current.x
        val y = current.y
        val id = current.id
        val color = colors[id % colors.size]

        paint.color = color
        paint.strokeWidth = width / 50f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.setShadowLayer(width / 25f, 0f, 0f, color)
        canvas.drawCircle(x, y, width / 7.7f, paint)

        /*paint.color = Color.WHITE
        paint.strokeWidth = 5f
        paint.style = Paint.Style.FILL
        paint.textSize = 75f
        canvas.drawText((id).toString(), x + 110, y, paint)*/
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
                coordinates.clear()
                coordinates.add(Pointer(event.getX(i), event.getY(i), winnerID))
                break
            } else if (i == winnerIndex) {
                winnerID = event.getPointerId(winnerIndex)
                coordinates.clear()
                coordinates.add(Pointer(event.getX(i), event.getY(i), winnerID))
                break
            }

        }

        if (lastPointersCount != pointerCount) {
            areYouAlone = false
            if(!isTimerSuccessEnded) startScheduleToRandom(pointerCount)
            if(lastPointersCount<pointerCount ) {MultiTouchActivity.currentTouches++
            Log.d("TOUCHES", "${MultiTouchActivity.currentTouches}")}
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
                    areYouAlone = true
                },
                2000,
                TimeUnit.MILLISECONDS
            )
        } else {
            val milliSeconds:Long = 1200
            future = scheduledService.schedule(
                {
                    isTimerSuccessEnded = true
                    winnerIndex = getOneRandomPointer()
                },
                milliSeconds,
                TimeUnit.MILLISECONDS
            )
            startTextTimer(milliSeconds)
        }
    }

    private fun isScheduleNotDone(): Boolean = future != null && !future!!.isDone

    private fun checkAndStopScheduleAndTextTimer() {

        if (isScheduleNotDone()) {
            future!!.cancel(false)
            textTimer?.cancel()
        }
    }

    private fun startTextTimer(milliSeconds:Long) {
        textTimer = CustomCountDownTimer(
            milliSeconds, 10, activity.findViewById(R.id.helpTextView), width
        )
            .start() as CustomCountDownTimer
    }

    private fun drawHelpText() {
        val helpTextView = activity.findViewById<TextView>(R.id.helpTextView)
        if (lastPointersCount == 0 || (lastPointersCount == 1 && !areYouAlone)) {
            helpTextView.textSize = width / 54f
            var helpText = resources.getString(R.string.helpText)
            helpText = helpText.substring(0, helpText.length-1) + " "
            when (MultiTouchActivity.mode) {
                "1" -> {
                    helpTextView.text = if(isTimerSuccessEnded) resources.getString(R.string.helpStartAgain)
                    else (helpText + resources.getString(R.string.helpWhoIsFirst).toLowerCase(Locale.getDefault()))
                }
                "123" -> helpTextView.text = (helpText + resources.getString(R.string.helpQueue).toLowerCase(Locale.getDefault()))
            }
        } else if (lastPointersCount == 1 && areYouAlone) {
            helpTextView.textSize = width / 54f
            helpTextView.text = resources.getString(R.string.youAreOnlyTheFirst)
        }

    }

    private fun getOneRandomPointer(): Int = (0 until lastPointersCount).random()

    private fun ahShitHereWeGoAgain() {
        colors.shuffle()
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



