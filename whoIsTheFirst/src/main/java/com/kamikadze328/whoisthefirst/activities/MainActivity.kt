package com.kamikadze328.whoisthefirst.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.whoisthefirst.MyApp
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.auxiliary_classes.checkUpdates
import com.kamikadze328.whoisthefirst.data.Mode
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        const val MODE_KEY = "mode"
    }

    @Inject
    lateinit var pref: SharedPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as MyApp).appComponent.injectActivity(this)

        checkUpdates(this)

        pref.onStartUp()

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
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }

            R.id.statisticsButton -> {
                startActivity(Intent(this, StatisticsActivity::class.java))
                true
            }

            R.id.aboutButton -> {
                startActivity(Intent(this, AboutInfoActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startWhoIsFirst() {
        Intent(this, MultiTouchActivity::class.java).apply {
            putExtra(MODE_KEY, Mode.ONE)
            startActivity(this)
        }
    }

    private fun startQueue() {
        Intent(this, MultiTouchActivity::class.java).apply {
            putExtra(MODE_KEY, Mode.QUEUE)
            startActivity(this)
        }
    }
}
