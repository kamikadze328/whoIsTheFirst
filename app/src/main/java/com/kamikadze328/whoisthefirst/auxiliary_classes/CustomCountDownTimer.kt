package com.kamikadze328.whoisthefirst.auxiliary_classes

import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import kotlin.math.roundToInt

class CustomCountDownTimer(millisInFuture: Long, countDownInterval: Long, var textView: TextView, var width:Int): CountDownTimer( millisInFuture, countDownInterval){
    override fun onTick(millisUntilFinished: Long) {
        var time:String = ((millisUntilFinished / 10f).roundToInt() /100f).toString()
        if(time.length<=3) time = time.plus(0)
        textView.textSize = width / 27f
        textView.text = time
        Log.d("KEK", time)
    }

    override fun onFinish() {
        textView.text = "Ты выйграл!"
    }

}