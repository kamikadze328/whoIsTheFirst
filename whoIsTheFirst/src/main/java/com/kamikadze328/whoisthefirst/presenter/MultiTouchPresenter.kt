package com.kamikadze328.whoisthefirst.presenter

import android.os.CountDownTimer
import com.kamikadze328.whoisthefirst.auxiliary_classes.Pointer
import com.kamikadze328.whoisthefirst.data.Mode
import com.kamikadze328.whoisthefirst.data.MultiTouchState
import com.kamikadze328.whoisthefirst.data.TIMEOUT_MS_ALONE
import com.kamikadze328.whoisthefirst.data.TextSize
import com.kamikadze328.whoisthefirst.repository.ResourceRepository
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiTouchPresenter @Inject constructor(
    private val sharedPrefsRepository: SharedPreferencesRepository,
    private val resourceRepository: ResourceRepository
) {
    var view: MultiTouchView? = null

    private var timer: CountDownTimer? = null

    private var pointers: MutableList<Pointer> = mutableListOf()

    private var winner: Pointer? = null

    private var state: MultiTouchState = MultiTouchState.DEFAULT
        set(value) {
            field = value
            view?.stateUpdated(value)
        }

    var mode: Mode? = null


    fun tryRestart(): Boolean {
        return if (state == MultiTouchState.FINISH) {
            view?.setBackButtonVisibility(false)
            ahShitHereWeGoAgain()
            true
        } else false
    }

    fun ahShitHereWeGoAgain() {
        resourceRepository.getColors()
        //colors.shuffle()
        state = MultiTouchState.DEFAULT
        winner = null
        stopTimer()
    }

    //TIMERS
    private fun stopTimer() = timer?.cancel()

    private fun startTimerToRandom() {
        stopTimer()
        val timeout = sharedPrefsRepository.timeout.toLong()
        timer = getTextTimer(timeout).start()
        state = MultiTouchState.TIMER.apply { payload = timeout }
    }

    private fun startAloneTimer() {
        stopTimer()
        timer = getAloneTimer().start()
        state = MultiTouchState.YOU_ARE_ALONE_TIMER
    }

    private fun getAloneTimer(timeout: Long = TIMEOUT_MS_ALONE) =
        object : CountDownTimer(timeout, timeout) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                state = MultiTouchState.YOU_ARE_ALONE
            }
        }

    private fun getTextTimer(timeout: Long) = object : CountDownTimer(timeout, 10) {
        override fun onTick(millisUntilFinished: Long) {
            if (state != MultiTouchState.TIMER) cancel()
            else {
                view?.stateUpdated(state.apply { payload = millisUntilFinished })
            }
        }

        override fun onFinish() {
            generateWinner()
        }
    }

    private fun generateWinner() {
        when (mode) {
            Mode.ONE -> {
                pointers[generateRandomIndexPointer(pointers.size)].let {
                    winner = it
                    sharedPrefsRepository.increaseAttemptsOne()
                }
                state = MultiTouchState.FINISH_BUT_WINNER_POINTER_IS_DOWN
                onFinishButWinnerPointerIsDown(pointers)
            }

            Mode.QUEUE -> {
                val queue = generateRandomQueue(pointers.size)
                queue.forEachIndexed { index, i -> pointers[i].placeInQueue = index }
                sharedPrefsRepository.increaseAttemptsQueue()
                state = MultiTouchState.FINISH
            }

            else -> {
            }
        }
        view?.setBackButtonVisibility(true)
        view?.drawTouches(pointers)
    }

    private fun generateRandomIndexPointer(pointerCount: Int) = (0 until pointerCount).random()
    private fun generateRandomQueue(pointerCount: Int) = (0 until pointerCount).shuffled()

    private fun checkTouchesCount(pointers: List<Pointer>): Boolean {
        val countNewPointers = pointers.size - this.pointers.size
        if (countNewPointers > 0 && state != MultiTouchState.FINISH_BUT_WINNER_POINTER_IS_DOWN) {
            sharedPrefsRepository.increaseTouches(countNewPointers.toLong())
        }
        return pointers.size != this.pointers.size
    }

    private fun onFinishButWinnerPointerIsDown(pointers: MutableList<Pointer>): Boolean {
        val winner = pointers.find { it.id == winner?.id }
        if (winner == null) {
            state = MultiTouchState.FINISH
            return true
        } else {
            this.winner = winner
            pointers.clear()
            pointers.add(winner)
        }
        return false
    }

    fun newLocalTouchEvent(pointers: MutableList<Pointer>) {
        if (state == MultiTouchState.FINISH) return

        val isCountTouchesChanged = checkTouchesCount(pointers)

        when {
            state == MultiTouchState.FINISH_BUT_WINNER_POINTER_IS_DOWN -> {
                val isFinish = onFinishButWinnerPointerIsDown(pointers)
                if (isFinish) return
            }

            pointers.size == 1 && state != MultiTouchState.YOU_ARE_ALONE_TIMER && state != MultiTouchState.YOU_ARE_ALONE -> {
                startAloneTimer()
            }

            pointers.size > 1 && isCountTouchesChanged -> {
                startTimerToRandom()
            }

            pointers.size == 0 && state != MultiTouchState.DEFAULT -> {
                stopTimer()
                state = MultiTouchState.DEFAULT
            }
        }

        this.pointers = pointers

        view?.drawTouches(pointers)
    }


}

interface MultiTouchView {
    fun setText(text: String, textSize: TextSize = TextSize.NORMAL)
    fun drawTouches(pointers: List<Pointer>)
    fun stateUpdated(state: MultiTouchState)
    fun setBackButtonVisibility(isVisible: Boolean)
}