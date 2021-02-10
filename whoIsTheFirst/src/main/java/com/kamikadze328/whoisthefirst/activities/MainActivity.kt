package com.kamikadze328.whoisthefirst.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.whoisthefirst.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object{
        const val CURRENT_TOUCHES_KEY = "current_touches"
        const val CURRENT_ATTEMPTS_KEY = "current_attempt"
    }

    private val resultRequest = 1
    private val extrasWhoIsFirst = "1"
    private val extrasSequence = "123"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // If the last instance was saved then just restore the textViews.
        // Else set the initial value for current and start new thread to get from the db the total max ever.
        if (savedInstanceState != null) {
            currentTouchesTextView.text = savedInstanceState.getString(CURRENT_TOUCHES_KEY)
            currentAttemptTextView.text = savedInstanceState.getString(CURRENT_ATTEMPTS_KEY)
        } else {
            currentAttemptTextView.text = "0"
            currentTouchesTextView.text = "0"
            //GetTotalMaxAsyncTask(applicationContext, this.totalAttempt).execute()
        }
    }

    fun startWhoIsFirst(view: View) {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra("mode", extrasWhoIsFirst)
        //intent.putExtra("currentTouches")
        startActivityForResult(intent, resultRequest)
    }

    fun startQueue(view: View) {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra("mode", extrasSequence)
        startActivityForResult(intent, resultRequest)
        val toast = Toast.makeText(
            applicationContext,
            resources.getString(R.string.queueIsNotAvailable), Toast.LENGTH_SHORT
        )

        toast.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resultTouches = data?.getIntExtra(CURRENT_TOUCHES_KEY, 0) ?:0
        if (requestCode == resultRequest) {
            if (resultCode == RESULT_OK) {
                currentTouchesTextView.text = resultTouches.toString()
                currentAttemptTextView.text = (currentAttemptTextView.text.toString().toInt() + 1).toString()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_TOUCHES_KEY, currentTouchesTextView.text.toString())
        outState.putString(CURRENT_ATTEMPTS_KEY, currentAttemptTextView.text.toString())
        super.onSaveInstanceState(outState)
    }
}
