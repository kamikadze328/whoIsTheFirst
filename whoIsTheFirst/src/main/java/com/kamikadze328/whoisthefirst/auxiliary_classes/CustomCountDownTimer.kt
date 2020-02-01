package com.kamikadze328.whoisthefirst.auxiliary_classes

import android.content.Context

import android.os.CountDownTimer
import android.widget.TextView
import com.kamikadze328.whoisthefirst.R
import java.util.*
import kotlin.math.roundToInt

class CustomCountDownTimer(
    private val mode:String,
    millisInFuture: Long,
    countDownInterval: Long,
    private val textView: TextView,
    private val  width:Int,
    private val context: Context)
    : CountDownTimer( millisInFuture, countDownInterval) {
    override fun onTick(millisUntilFinished: Long) {
        var time: String = ((millisUntilFinished / 10f).roundToInt() / 100f).toString()
        if (time.length <= 3) time = time.plus(0)
        textView.textSize = width / 18f
        textView.text = time
    }

    override fun onFinish() {
        when(mode){
            "1" -> {
                if(Locale.getDefault().language=="ru") textView.textSize = width / 25f
                textView.text = context.resources.getString(R.string.youWin)
            }
            "123" -> {
                textView.textSize = width / 30f
                textView.text = context.resources.getString(R.string.helpStartAgainQueue)
            }
        }
    }

}