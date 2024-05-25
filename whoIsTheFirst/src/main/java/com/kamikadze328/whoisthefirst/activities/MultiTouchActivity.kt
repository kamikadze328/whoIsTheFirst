package com.kamikadze328.whoisthefirst.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.kamikadze328.whoisthefirst.MyApp
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.auxiliary_classes.ActivityUtils.getSerializable
import com.kamikadze328.whoisthefirst.auxiliary_classes.Pointer
import com.kamikadze328.whoisthefirst.data.Mode
import com.kamikadze328.whoisthefirst.data.MultiTouchState
import com.kamikadze328.whoisthefirst.data.TextSize
import com.kamikadze328.whoisthefirst.data.TouchEventMapper
import com.kamikadze328.whoisthefirst.presenter.MultiTouchPresenter
import com.kamikadze328.whoisthefirst.presenter.MultiTouchView
import com.kamikadze328.whoisthefirst.views.MultiTouchCustomView
import javax.inject.Inject


class MultiTouchActivity : AppCompatActivity(R.layout.activity_multi_touch), MultiTouchView {
    private var mode = Mode.ONE
    private var state: MultiTouchState = MultiTouchState.DEFAULT

    @Inject
    lateinit var presenter: MultiTouchPresenter

    @Inject
    lateinit var touchEventMapper: TouchEventMapper

    private val backButton: ImageButton by lazy { findViewById(R.id.backButton) }
    private val mainView: MultiTouchCustomView by lazy { findViewById(R.id.multitouchCustomView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MyApp).appComponent.injectActivity(this)
        presenter.view = this

        setupMode()

        presenter.ahShitHereWeGoAgain()

        setupOnTouch()
        addDoubleTapListener()
        setupBackButton()
        hideSystemUI()

    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //if i hide all system ui, users can think that phone is broken or app is a virus.
        /*WindowInsetsControllerCompat(window, findViewById(R.id.multitouchRoot)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupOnTouch() {
        val gestureDetector =
            GestureDetector(mainView.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    return mainView.notifyAllDoubleTap()
                }
            })

        mainView.setOnTouchListener { v, event ->
            val pointers = touchEventMapper.map(event, v.width, v.height)
            presenter.newLocalTouchEvent(pointers)
            gestureDetector.onTouchEvent(event)
            true
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.view = null
    }

    private fun addDoubleTapListener() {
        mainView.addDoubleTapListener(presenter::tryRestart)
    }

    private fun setupMode() {
        mode = intent.getSerializable(MainActivity.MODE_KEY) ?: Mode.ONE
        presenter.mode = mode
    }

    private fun setupBackButton() {
        backButton.setOnClickListener {
            onBackPressedDispatcher.addCallback(this) { finish() }.handleOnBackPressed()
        }
    }

    override fun setBackButtonVisibility(isVisible: Boolean) {
        backButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setTextSize(dimenRes: Int) = mainView.setTextSize(dimenRes)


    override fun setText(text: String, textSize: TextSize) {
        mainView.setText(text)
        setTextSize(textSize.dimenId)
    }


    override fun drawTouches(pointers: List<Pointer>) {
        val relativePointers = touchEventMapper.map(pointers, mainView.width, mainView.height)
        if (state == MultiTouchState.FINISH && mode == Mode.QUEUE)
            mainView.drawTouchesWithCircles(relativePointers)
        else
            mainView.drawTouches(relativePointers)
    }

    private fun makeDefaultText(dirtyText: String): String {
        val justText = dirtyText.dropLast(1)
        val additionalText = when (mode) {
            Mode.ONE -> getString(R.string.helpWhoIsFirst)
            Mode.QUEUE -> getString(R.string.helpQueue)
            else -> ""
        }.lowercase()
        return "$justText $additionalText"
    }

    private fun getWinnerText(): String = when (mode) {
        Mode.ONE -> {
            getString(R.string.youWin)
        }

        Mode.QUEUE -> {
            getString(R.string.helpStartAgain)
        }

        else -> {
            getString(R.string.youWin)
        }
    }

    override fun stateUpdated(state: MultiTouchState) {
        this.state = state
        when (if (state == MultiTouchState.YOU_ARE_ALONE_TIMER) MultiTouchState.DEFAULT else state) {
            MultiTouchState.FINISH_BUT_WINNER_POINTER_IS_DOWN -> {
                val text = getWinnerText()
                mainView.setText(text, state.textSize)
            }

            MultiTouchState.DEFAULT -> {
                val text = makeDefaultText(getString(state.textRes))
                mainView.setText(text, state.textSize)
            }

            MultiTouchState.TIMER -> {
                val text = "%.2f".format((state.payload as Long) * 1.0 / 1000)
                mainView.setText(text, state.textSize)
            }

            else -> {
                mainView.setText(state.textRes, state.textSize)
            }
        }
    }

}