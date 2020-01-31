package com.kamikadze328.whoisthefirst.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
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
    private var futureTask: ScheduledFuture<*>? = null
    private var textTimer: CustomCountDownTimer? = null

    private var coordinates: ArrayList<Pointer> = ArrayList()

    private val paint: Paint = Paint()

    private var areYouAlone = false
    private var isTimerSuccessEnded = false
    private var lastPointersCount = 0
    private var winnerID: Int = -1

    private val milliSecondsForTimer: Long = 1000
    private val milliSecondsForOne: Long = 2500
    private var radiusCircle = 0f

    private val mode = MultiTouchActivity.mode

    init {
        colors.shuffle()
        colors.shuffle()
        colors.shuffle()
        val gestureDetector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                return if(isTimerSuccessEnded && mode=="123"){
                    ahShitHereWeGoAgain()
                    true
                } else false
            }
        })
        this.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        radiusCircle = (height / 2) / 7.7f
    }

    private fun drawTouches(
        canvas: Canvas,
        coordinates: MutableList<Pointer>
    ) {

        canvas.drawColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        if (!isTimerSuccessEnded || mode == "1") coordinates.forEach { drawCircle(canvas, it.x, it.y, it.id) }
        else coordinates.forEach { drawOneFromQueue(canvas, it) }
    }

    private fun drawCircle(canvas: Canvas, x: Float, y:Float, id:Int) {
        val color = colors[id % colors.size]

        paint.color = color
        paint.strokeWidth = width / 50f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.setShadowLayer(width / 25f, 0f, 0f, color)
        canvas.drawCircle(x, y, radiusCircle, paint)
    }

    private fun drawOneFromQueue(canvas: Canvas, current: Pointer) {
        val x = current.x
        val y = current.y
        val placeInLine = (current.placeInLine + 1).toString()

        drawCircle(canvas, x, y, current.id)

        //paint.color = colors[current.id % colors.size]

        paint.strokeWidth = width / 150f
        paint.textSize = width / 20f
        val vOffset = - width / 50f

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        paint.setShadowLayer(0f, 0f, 0f, 0)

        //Numbers around a Circle
        val path = Path()
        path.addCircle(x, y, radiusCircle, Path.Direction.CW)
        canvas.drawTextOnPath(placeInLine, path, 0f, vOffset, paint)

        canvas.drawTextOnPath(placeInLine, path, (radiusCircle * 2 * Math.PI.toFloat()) * 0 / 7, vOffset, paint)
        canvas.drawTextOnPath(placeInLine, path, (radiusCircle * 2 * Math.PI.toFloat()) * 1 / 7, vOffset, paint)
        canvas.drawTextOnPath(placeInLine, path, (radiusCircle * 2 * Math.PI.toFloat()) * 2 / 7, vOffset, paint)
        canvas.drawTextOnPath(placeInLine, path, (radiusCircle * 2 * Math.PI.toFloat()) * 3 / 7, vOffset, paint)
        canvas.drawTextOnPath(placeInLine, path, (radiusCircle * 2 * Math.PI.toFloat()) * -1 / 7, vOffset, paint)
        canvas.drawTextOnPath(placeInLine, path, (radiusCircle * 2 * Math.PI.toFloat()) * -2 / 7, vOffset, paint)
        canvas.drawTextOnPath(placeInLine, path, (radiusCircle * 2 * Math.PI.toFloat()) * -3 / 7, vOffset, paint)

        //Number inside a circle
        paint.textSize = width / 6f
        canvas.drawText(placeInLine, x, y+width / 17f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (mode=="123" && isTimerSuccessEnded) {
            return true
        }
        coordinates = ArrayList()
        val pointerCount = event.pointerCount
        // For each of the pointers of touches -> Put their coordinates to draw them in onDraw().
        for (i in 0 until pointerCount) {

            if (!isTimerSuccessEnded) {
                coordinates.add(Pointer(event.getX(i), event.getY(i), event.getPointerId(i), 0))

            } else if (mode=="1") {
                if (winnerID != event.getPointerId(i)) continue
                coordinates.add(Pointer(event.getX(i), event.getY(i), winnerID, 0))
                break
            }
        }

        //if action is up(and it was the last pointer) pointerCount = 1
        if (lastPointersCount != pointerCount) {
            areYouAlone = false

            lastPointersCount = pointerCount

            if (!isTimerSuccessEnded) {
                startScheduleToRandom(pointerCount)
            }
            if (lastPointersCount < pointerCount) {
                MultiTouchActivity.currentTouches++
            }
            if (coordinates.size == 0 && mode == "1") {
                val winnerIndex = generateIndexRandomPointer()
                winnerID = event.getPointerId(winnerIndex)
                coordinates.add(Pointer(event.getX(winnerIndex), event.getY(winnerIndex), winnerID, 0))
            }
        }

        this.invalidate()

        // If the last touch pointer is removed -> remove its circle.
        if (event.action == MotionEvent.ACTION_UP) {
            ahShitHereWeGoAgain()
            return false
        }
        return true
    }

    private fun startScheduleToRandom(countPointer: Int) {
        checkAndStopScheduleAndTextTimer()
        if (countPointer == 1) {
            futureTask = scheduledService.schedule(
                {
                    areYouAlone = true
                    this.invalidate()
                },
                milliSecondsForOne,
                TimeUnit.MILLISECONDS
            )
        } else {
            futureTask = scheduledService.schedule(
                {
                    when (mode) {
                        "1" -> {
                            winnerID = coordinates[generateIndexRandomPointer()].id
                            val c = (coordinates.filter { it.id == winnerID } as ArrayList<Pointer>)
                            coordinates = ArrayList(c)
                        }
                        "123" -> {
                            val queue = generateRandomQueue()
                            coordinates.forEach { it.placeInLine = queue[coordinates.indexOf(it)] }
                            activity.runOnUiThread { activity.addBackButton() }
                        }
                    }
                    isTimerSuccessEnded = true
                    this.invalidate()
                },
                milliSecondsForTimer,
                TimeUnit.MILLISECONDS
            )
            startTextTimer(milliSecondsForTimer)
        }
    }

    private fun isScheduleNotDone(): Boolean = futureTask != null && !futureTask!!.isDone

    private fun checkAndStopScheduleAndTextTimer() {

        if (isScheduleNotDone()) {
            futureTask!!.cancel(false)
            textTimer?.cancel()
        }
    }

    private fun startTextTimer(milliSeconds: Long) {

        textTimer = CustomCountDownTimer(mode,
            milliSeconds, 10, activity.findViewById(R.id.helpTextView), width, context
        )
            .start() as CustomCountDownTimer
    }

    private fun drawHelpText() {
        val helpTextView = activity.findViewById<TextView>(R.id.helpTextView)
        if (lastPointersCount == 0 || (lastPointersCount == 1 && !areYouAlone)) {
            helpTextView.textSize = width / 50f
            var helpText = resources.getString(R.string.helpText)
            helpText = helpText.substring(0, helpText.length - 1) + " "
            when (mode) {
                "1" -> {
                    helpTextView.text =
                        if (isTimerSuccessEnded) resources.getString(R.string.helpStartAgain)
                        else (helpText + context.resources.getString(R.string.helpWhoIsFirst).toLowerCase(
                            Locale.getDefault()
                        ))
                }
                "123" -> helpTextView.text =
                    (helpText + resources.getString(R.string.helpQueue).toLowerCase(Locale.getDefault()))
            }
        } else if (lastPointersCount == 1 && areYouAlone) {
            helpTextView.textSize = width / 50f
            helpTextView.text = resources.getString(R.string.youAreOnlyTheFirst)
        }
    }

    private fun generateIndexRandomPointer(): Int = (0 until lastPointersCount).random()

    private fun generateRandomQueue(): MutableList<Int>{
        val indexRandomQueue = MutableList(lastPointersCount) { i -> i}
        indexRandomQueue.shuffle()
        indexRandomQueue.shuffle()
        indexRandomQueue.shuffle()
        return indexRandomQueue
    }

    fun ahShitHereWeGoAgain() {
        colors.shuffle()
        isTimerSuccessEnded = false
        areYouAlone = false
        lastPointersCount=0
        checkAndStopScheduleAndTextTimer()
        try {
            coordinates.removeAt(0)
        } catch (e: Exception) {
        }
    }

}



