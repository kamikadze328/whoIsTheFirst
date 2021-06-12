package com.kamikadze328.whoisthefirst.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.views.MultiTouchCustomView


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
        val helpTextView: TextView = findViewById(R.id.helpTextView)
        when(mode){
            MainActivity.extrasWhoIsFirst -> helpTextView.text = getString(R.string.helpWhoIsFirst)
            MainActivity.extrasSequence -> helpTextView.text = getString(R.string.helpQueue)
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
        output.putExtra(MainActivity.MODE_KEY, mode)
        findViewById<MultiTouchCustomView>(R.id.customView).ahShitHereWeGoAgain()
        setResult(RESULT_OK, output)
        finish()
        super.onBackPressed()
    }

    fun onBackPressed(@Suppress("UNUSED_PARAMETER") view: View){
        hideBackButton()
        onBackPressed()
    }

    fun addBackButton(){
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.isEnabled = true
        backButton.visibility = View.VISIBLE
    }
    fun hideBackButton(){
        val backButton: ImageButton = findViewById(R.id.backButton)
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