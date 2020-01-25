package com.kamikadze328.whoisfirst.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.whoisfirst.R


class MainActivity : AppCompatActivity() {

    companion object{
        const val currentTouchesKey = "current_touches"
        const val currentAttemptKey = "current_attempt"
    }

    private lateinit var currentTouches: TextView
    private lateinit var currentAttempt: TextView

    private val resultRequest = 1
    private val extrasWhoIsFirst = "1"
    private val extrasSequence = "123"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentTouches = findViewById(R.id.currentTouchesTextView)
        currentAttempt = findViewById(R.id.currentAttemptTextView)

        // If the last instance was saved then just restore the textViews.
        // Else set the initial value for current and start new thread to get from the db the total max ever.
        if (savedInstanceState != null) {
            currentTouches.text = savedInstanceState.getString(currentTouchesKey)
            currentAttempt.text = savedInstanceState.getString(currentAttemptKey)
        } else {
            currentAttempt.text = "0"
            currentTouches.text = "0"
            //GetTotalMaxAsyncTask(applicationContext, this.totalAttempt).execute()
        }
    }

    fun startWhoIsFirst(view: View) {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra("mode", extrasWhoIsFirst)
        startActivityForResult(intent, resultRequest)
    }

    fun startSequence(view: View) {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra("mode", extrasSequence)
        startActivityForResult(intent, resultRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resultTouches = data?.getIntExtra(currentTouchesKey, 0) ?:0
        if (requestCode == resultRequest) {
            if (resultCode == RESULT_OK) {
                currentTouches.text = resultTouches.toString()
                currentAttempt.text = (currentAttempt.text.toString().toInt() + 1).toString()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(currentTouchesKey, currentTouches.text.toString())
        outState.putString(currentAttemptKey, currentAttempt.text.toString())
        super.onSaveInstanceState(outState)
    }
}
