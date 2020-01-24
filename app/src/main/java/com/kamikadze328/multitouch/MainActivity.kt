package com.kamikadze328.multitouch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var totalAttempt: TextView
    private lateinit var currentAttempt: TextView

    private val TOTAL_ATTEMPT_KEY = "total_attempt"
    private val CURRENT_ATTEMPT_KEY = "current_attempt"
    private val RESULT_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        totalAttempt = findViewById(R.id.totalAttemptView)
        currentAttempt = findViewById(R.id.currentAttemptView)

        // If the last instance was saved then just restore the textViews.
        // Else set the initial value for current and start new thread to get from the db the total max ever.
        if (savedInstanceState != null) {
            this.totalAttempt.setText(savedInstanceState.getString(MainActivity.TOTAL_ATTEMPT_KEY))
            this.currentAttempt.setText(savedInstanceState.getString(MainActivity.CURRENT_ATTEMPT_KEY))
        } else {
            this.currentAttempt.setText("0")
            GetTotalMaxAsyncTask(applicationContext, this.totalAttempt).execute()
        }
    }

    fun startMultiTouch(view: View?) {
        val intent = Intent(this, MultiTouchActivity::class.java)
        startActivityForResult(intent, RESULT_REQUEST)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TOTAL_ATTEMPT_KEY, this.totalAttempt.getText().toString())
        outState.putString(CURRENT_ATTEMPT_KEY, this.currentAttempt.getText().toString())
        super.onSaveInstanceState(outState)
    }
}
