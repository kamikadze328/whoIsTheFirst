package com.kamikadze328.whoisthefirst.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kamikadze328.whoisthefirst.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_multi_touch.*


class MultiTouchActivity:Activity(){
    companion object{
        var mode = ""
    }

    private var touchesCount = 0
    private var attemptsCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mode = intent.getStringExtra("mode")?:""
        setContentView(R.layout.activity_multi_touch)
        when(mode){
            "1" -> helpTextView.text = getString(R.string.helpWhoIsFirst)
            "123" -> helpTextView.text = getString(R.string.helpQueue)
        }
        if (savedInstanceState != null) {
            touchesCount = savedInstanceState.getInt(MainActivity.CURRENT_TOUCHES_KEY)
            attemptsCount = savedInstanceState.getInt(MainActivity.CURRENT_ATTEMPTS_KEY)
        }
    }


    override fun onBackPressed() {
        val output = Intent()
        output.putExtra(MainActivity.CURRENT_TOUCHES_KEY, touchesCount)
        output.putExtra(MainActivity.CURRENT_ATTEMPTS_KEY, attemptsCount)
        customView.ahShitHereWeGoAgain()
        setResult(RESULT_OK, output)
        finish()
        super.onBackPressed()
    }

    override fun onPause() {
        //MainActivity.saveSta
        super.onPause()
    }

    fun onBackPressed(@Suppress("UNUSED_PARAMETER") view: View){
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
    fun incrementTouchesCount(){
        touchesCount++
    }
    fun incrementAttemptsCount(){
        attemptsCount++
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(MainActivity.CURRENT_TOUCHES_KEY, touchesCount)
        outState.putInt(MainActivity.CURRENT_ATTEMPTS_KEY, attemptsCount)
        super.onSaveInstanceState(outState)
    }

}