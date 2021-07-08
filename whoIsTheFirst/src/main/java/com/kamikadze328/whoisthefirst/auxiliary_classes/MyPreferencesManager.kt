package com.kamikadze328.whoisthefirst.auxiliary_classes

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.kamikadze328.whoisthefirst.R

class MyPreferencesManager(val context: Context) {
    private val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val touchesTotalId = R.string.number_touches_total_key
    private val touchesCurrentId = R.string.number_touches_current_key

    private val attemptsTotalId = R.string.number_attemps_total_key
    private val attemptsCurrentId = R.string.number_attemps_current_key

    private val attemptsOneTotalId = R.string.number_attemps_the_first_total_key
    private val attemptsOneCurrentId = R.string.number_attemps_the_first_current_key

    private val attemptsQueueTotalId = R.string.number_attemps_queue_total_key
    private val attemptsQueueCurrentId = R.string.number_attemps_queue_current_key

    private val isFirstTimeId = R.string.first_time_run

    var touchesCurrent: Long
        get() = get(touchesCurrentId)
        set(value) = set(touchesCurrentId, value)

    var attemptsCurrent: Long
        get() = get(attemptsCurrentId)
        set(value) = set(attemptsCurrentId, value)

    var attemptsOneCurrent: Long
        get() = get(attemptsOneCurrentId)
        set(value) = set(attemptsOneCurrentId, value)

    var attemptsQueueCurrent: Long
        get() = get(attemptsQueueCurrentId)
        set(value) = set(attemptsQueueCurrentId, value)

    var touchesTotal: Long
        get() = get(touchesTotalId)
        set(value) = set(touchesTotalId, value)

    var attemptsTotal: Long
        get() = get(attemptsTotalId)
        set(value) = set(attemptsTotalId, value)

    var attemptsOneTotal: Long
        get() = get(attemptsOneTotalId)
        set(value) = set(attemptsOneTotalId, value)

    var attemptsQueueTotal: Long
        get() = get(attemptsQueueTotalId)
        set(value) = set(attemptsQueueTotalId, value)

    var isFirstTime: Boolean
        get() = pref.getBoolean(context.resources.getString(isFirstTimeId), true)
        set(value) = set(R.string.first_time_run, value)

    private fun set(strId: Int, value: Long) {
        with(pref.edit()) {
            putLong(context.resources.getString(strId), value)
            apply()
        }
    }

    private fun set(strId: Int, value: Boolean) {
        with(pref.edit()) {
            putBoolean(context.resources.getString(strId), value)
            apply()
        }
    }

    private fun get(strId: Int): Long {
        return pref.getLong(context.resources.getString(strId), 0L)
    }

    fun increaseAllTouches(value: Long) {
        touchesCurrent += value
        touchesTotal += value
    }

    fun increaseAllAttempts(value: Long) {
        attemptsCurrent += value
        attemptsTotal += value
    }

    fun increaseAllAttemptsOne(value: Long) {
        attemptsOneCurrent += value
        attemptsOneTotal += value
    }

    fun increaseAllAttemptsQueue(value: Long) {
        attemptsQueueCurrent += value
        attemptsQueueTotal += value
    }

}

