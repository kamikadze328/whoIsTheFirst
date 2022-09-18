package com.kamikadze328.whoisthefirst.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.kamikadze328.whoisthefirst.BuildConfig
import com.kamikadze328.whoisthefirst.R


class AboutInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_info)

        findViewById<TextView>(R.id.privacy_policy_ref).movementMethod =
            LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.github_ref).movementMethod = LinkMovementMethod.getInstance()
        findViewById<TextView>(R.id.app_version).text = resources.getString(
            R.string.app_version,
            BuildConfig.VERSION_NAME
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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