package com.kamikadze328.whoisthefirst.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kamikadze328.whoisthefirst.R
import kotlinx.android.synthetic.main.activity_multi_touch.*


class MultiTouchActivity:Activity(){
    companion object{
        var mode = ""
        var currentTouches = 0
    }
    private var currentAttempt = 0
    private var localCurrentTouches = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mode = intent.getStringExtra("mode")?:""
        setContentView(R.layout.activity_multi_touch)
        when(mode){
            "1"   ->   helpTextView.text = getString(R.string.helpWhoIsFirst)
            "123" ->   helpTextView.text = getString(R.string.helpQueue)
        }

        if (savedInstanceState != null) {
            currentTouches = savedInstanceState.getString(MainActivity.CURRENT_TOUCHES_KEY) ?.toInt() ?: 0
            currentAttempt = savedInstanceState.getString(MainActivity.CURRENT_ATTEMPTS_KEY) ?.toInt() ?: 0

        } else {
            currentTouches = localCurrentTouches
            Log.d("MultiACTIVITYNULL", "$currentTouches")
        }
    }


    override fun onBackPressed() {
        val output = Intent()
        output.putExtra(MainActivity.CURRENT_TOUCHES_KEY, currentTouches)
        output.putExtra(MainActivity.CURRENT_ATTEMPTS_KEY, currentAttempt+1)
        customView.ahShitHereWeGoAgain()
        setResult(RESULT_OK, output)
        finish()
        super.onBackPressed()
    }

    fun onBackPressed(@Suppress("UNUSED_PARAMETER")view: View){
        hideBackButton()
        onBackPressed()
    }

    fun addBackButton(){
        backButton.isEnabled = true
        backButton.visibility = View.VISIBLE
    }
    fun hideBackButton(){
        backButton.isEnabled = false
        backButton.visibility = View.INVISIBLE
    }


}