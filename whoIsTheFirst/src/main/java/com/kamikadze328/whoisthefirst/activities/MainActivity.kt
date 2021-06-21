package com.kamikadze328.whoisthefirst.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.auxiliary_classes.MyPreferencesManager

class MainActivity : AppCompatActivity() {

    companion object {
        const val CURRENT_TOUCHES_KEY = "current_touches"
        const val CURRENT_ATTEMPTS_KEY = "current_attempt"
        const val MODE_KEY = "mode"
        const val extrasWhoIsFirst = "1"
        const val extrasSequence = "123"
    }

    private lateinit var pref: MyPreferencesManager

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            updateStatistics(result.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pref = MyPreferencesManager(applicationContext)

        if (pref.isFirstTime) {
            pref.touchesTotal = 0L
            pref.attemptsTotal = 0L
            pref.attemptsOneTotal = 0L
            pref.attemptsQueueTotal = 0L
            pref.isFirstTime = false
        }
        pref.touchesCurrent = 0L
        pref.attemptsCurrent = 0L
        pref.attemptsOneCurrent = 0L
        pref.attemptsQueueCurrent = 0L

        findViewById<Button>(R.id.whoIsFirstButton).setOnClickListener { startWhoIsFirst() }
        findViewById<Button>(R.id.queueButton).setOnClickListener { startQueue() }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settingsButton -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                return true
            }
            R.id.statisticsButton -> {
                startActivity(Intent(this@MainActivity, StatisticsActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startWhoIsFirst() {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra(MODE_KEY, extrasWhoIsFirst)
        resultLauncher.launch(intent)
    }

    private fun startQueue() {
        val intent = Intent(this, MultiTouchActivity::class.java)
        intent.putExtra(MODE_KEY, extrasSequence)
        resultLauncher.launch(intent)
    }

    private fun updateStatistics(data: Intent?) {
        data?.let {
            val resultTouches = it.getIntExtra(CURRENT_TOUCHES_KEY, 0).toLong()
            val resultAttempts = it.getIntExtra(CURRENT_ATTEMPTS_KEY, 0).toLong()
            val mode = it.getStringExtra(MODE_KEY)

            pref.increaseAllTouches(resultTouches)
            pref.increaseAllAttempts(resultAttempts)
            when (mode) {
                extrasSequence -> pref.increaseAllAttemptsQueue(resultAttempts)
                extrasWhoIsFirst -> pref.increaseAllAttemptsOne(resultAttempts)
            }
        }

    }
}
