package com.kamikadze328.whoisthefirst.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.whoisthefirst.MyApp
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepository
import com.kamikadze328.whoisthefirst.views.StatisticsGroup
import javax.inject.Inject

class StatisticsActivity : AppCompatActivity(R.layout.activity_statistics) {

    @Inject
    lateinit var pref: SharedPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (applicationContext as MyApp).appComponent.injectActivity(this)

        setValues(findViewById(R.id.stats_total), true)
        setValues(findViewById(R.id.stats_current), false)

    }

    private fun setValues(view: StatisticsGroup, isTotal: Boolean = false) {
        val touches = if (isTotal) pref.touchesTotal else pref.touchesCurrent
        val attemptsOne = if (isTotal) pref.attemptsOneTotal else pref.attemptsOneCurrent
        val attemptsQueue = if (isTotal) pref.attemptsQueueTotal else pref.attemptsQueueCurrent
        val attempts = attemptsOne + attemptsQueue

        view.setTouchesValue(touches)
        view.setAttemptsOneValue(attemptsOne)
        view.setAttemptsQueueValue(attemptsQueue)
        view.setAttemptsValue(attempts)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.addCallback(this) { finish() }.handleOnBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}