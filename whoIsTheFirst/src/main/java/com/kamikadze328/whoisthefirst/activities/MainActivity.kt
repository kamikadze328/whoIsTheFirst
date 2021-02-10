package com.kamikadze328.whoisthefirst.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.whoisthefirst.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_TOUCHES_KEY = "current_touches"
        const val CURRENT_ATTEMPTS_KEY = "current_attempt"
    }

    private val activityRequestCode = 1
    private val extrasWhoIsFirst = "1"
    private val extrasSequence = "123"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            currentTouchesTextView.text = savedInstanceState.getString(CURRENT_TOUCHES_KEY)
            currentAttemptTextView.text = savedInstanceState.getString(CURRENT_ATTEMPTS_KEY)
        } else {
            currentTouchesTextView.text = "0"
            currentAttemptTextView.text = "0"
        }
    }

    fun startWhoIsFirst(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra("mode", extrasWhoIsFirst)
        startActivityForResult(intent, activityRequestCode)
    }

    fun startQueue(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra("mode", extrasSequence)
        startActivityForResult(intent, activityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == activityRequestCode && resultCode == RESULT_OK) {
            var resultTouches = data?.getIntExtra(CURRENT_TOUCHES_KEY, 0) ?: 0
            var resultAttempts = data?.getIntExtra(CURRENT_ATTEMPTS_KEY, 0) ?: 0

            resultTouches += getTouchesCountInt()
            resultAttempts += getAttemptsCountInt()

            currentTouchesTextView.text = resultTouches.toString()
            currentAttemptTextView.text = resultAttempts.toString()
            saveStatistics(resultTouches, resultAttempts)
        }
    }

    private fun saveStatistics(touchesCount: Int, attemptCount: Int) {
        val ed: Editor = getPreferences(MODE_PRIVATE).edit()
        ed.putInt(CURRENT_TOUCHES_KEY, touchesCount)
        ed.putInt(CURRENT_ATTEMPTS_KEY, attemptCount)
        ed.apply()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_TOUCHES_KEY, getTouchesCount())
        outState.putString(CURRENT_ATTEMPTS_KEY, getAttemptsCount())
        super.onSaveInstanceState(outState)
    }

    private fun getTouchesCountInt(): Int {
        return getTouchesCount().toInt()
    }

    private fun getAttemptsCountInt(): Int {
        return getAttemptsCount().toInt()
    }

    private fun getTouchesCount(): String {
        return currentTouchesTextView.text.toString()
    }

    private fun getAttemptsCount(): String {
        return currentAttemptTextView.text.toString()
    }
}
