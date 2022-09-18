package com.kamikadze328.whoisthefirst.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepositoryImpl.Companion.TIMEOUT_DEFAULT
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepositoryImpl.Companion.TIMEOUT_MAX
import com.kamikadze328.whoisthefirst.repository.SharedPreferencesRepositoryImpl.Companion.TIMEOUT_MIN

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
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

    class SettingsFragment : PreferenceFragmentCompat() {
        companion object {
            private const val SEEK_BAR_STEP = 100
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            val timeoutPrefs =
                findPreference<SeekBarPreference>(resources.getString(R.string.timeout_key))

            timeoutPrefs?.apply {

                summary = resources.getString(R.string.timeout_summary, TIMEOUT_DEFAULT)
                //setDefaultValue(TIMEOUT_DEFAULT)
                min = TIMEOUT_MIN
                max = TIMEOUT_MAX

                onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        (preference as SeekBarPreference).value =
                            (newValue as Int / SEEK_BAR_STEP) * SEEK_BAR_STEP
                        false

                    }
            }
        }
    }

}