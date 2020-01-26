package com.kamikadze328.whoisthefirst.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.kamikadze328.whoisthefirst.R
import kotlinx.android.synthetic.main.activity_multi_touch.*


class MultiTouchActivity:Activity(){
    companion object{
        var mode = ""
    }
    private var currentTouches = 0
    private var currentAttempt =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_touch)
        mode = intent.getStringExtra("mode")?:""
        when(mode){
            "1"   ->   helpTextView.text = getString(R.string.helpWhoIsFirst)
            "123" ->   helpTextView.text = getString(R.string.helpQueue)
        }

        if (savedInstanceState != null) {
            currentTouches = savedInstanceState.getString(MainActivity.currentTouchesKey) ?.toInt() ?: 0
            currentAttempt = savedInstanceState.getString(MainActivity.currentAttemptKey) ?.toInt() ?: 0

        } else {
            currentTouches = 0
            //GetTotalTouchesAsyncTask(applicationContext, totalCount).execute()
        }
    }


    override fun onBackPressed() {
        val output = Intent()
        output.putExtra(
            MainActivity.currentTouchesKey,
            currentTouches
        )
        output.putExtra(MainActivity.currentAttemptKey, currentAttempt+1)
        setResult(RESULT_OK, output)
        finish()
        super.onBackPressed()
    }
}