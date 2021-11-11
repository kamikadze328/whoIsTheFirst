package com.kamikadze328.whoisthefirst.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.kamikadze328.whoisthefirst.R
import javax.inject.Inject

interface SharedPreferencesRepository {
    fun increaseTouches(value: Long = 1L)
    fun increaseAttemptsOne(value: Long = 1L)
    fun increaseAttemptsQueue(value: Long = 1L)

    fun onStartUp()

    var touchesCurrent: Long
    var attemptsOneCurrent: Long
    var attemptsQueueCurrent: Long
    var touchesTotal: Long
    var attemptsOneTotal: Long
    var attemptsQueueTotal: Long

    var isFirstTime: Boolean

    var timeout: Int
}

class SharedPreferencesRepositoryImpl @Inject constructor(val context: Context) :
    SharedPreferencesRepository {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        const val TIMEOUT_DEFAULT = 2000
        const val TIMEOUT_MIN = 1000
        const val TIMEOUT_MAX = 10000
    }

    private val touchesTotalId = R.string.number_touches_total_key
    private val touchesCurrentId = R.string.number_touches_current_key

    private val attemptsOneTotalId = R.string.number_attemps_the_first_total_key
    private val attemptsOneCurrentId = R.string.number_attemps_the_first_current_key

    private val attemptsQueueTotalId = R.string.number_attemps_queue_total_key
    private val attemptsQueueCurrentId = R.string.number_attemps_queue_current_key

    private val isFirstTimeId = R.string.first_time_run

    private val timeoutId = R.string.timeout_key

    override var touchesCurrent: Long
        get() = get(touchesCurrentId)
        set(value) = set(touchesCurrentId, value)

    override var attemptsOneCurrent: Long
        get() = get(attemptsOneCurrentId)
        set(value) = set(attemptsOneCurrentId, value)

    override var attemptsQueueCurrent: Long
        get() = get(attemptsQueueCurrentId)
        set(value) = set(attemptsQueueCurrentId, value)

    override var touchesTotal: Long
        get() = get(touchesTotalId)
        set(value) = set(touchesTotalId, value)

    override var attemptsOneTotal: Long
        get() = get(attemptsOneTotalId)
        set(value) = set(attemptsOneTotalId, value)

    override var attemptsQueueTotal: Long
        get() = get(attemptsQueueTotalId)
        set(value) = set(attemptsQueueTotalId, value)

    override var isFirstTime: Boolean
        get() = getBoolean(isFirstTimeId, true)
        set(value) = set(R.string.first_time_run, value)

    override var timeout: Int
        get() = getInt(timeoutId, TIMEOUT_DEFAULT)
        set(value) = setInt(timeoutId, value)

    private fun set(strId: Int, value: Long) {
        with(prefs.edit()) {
            putLong(context.resources.getString(strId), value)
            apply()
        }
    }

    private fun set(strId: Int, value: Boolean) {
        with(prefs.edit()) {
            putBoolean(context.resources.getString(strId), value)
            apply()
        }
    }

    private fun setInt(strId: Int, value: Int) {
        with(prefs.edit()) {
            putInt(context.resources.getString(strId), value)
            apply()
        }
    }

    private fun putLongs(vararg values: Pair<Int, Long>) {
        with(prefs.edit()) {
            values.forEach {
                putLong(context.resources.getString(it.first), it.second)
            }
            apply()
        }
    }

    private fun increaseLongs(vararg values: Pair<Int, Long>) {
        with(prefs.edit()) {
            values.forEach {
                putLong(context.resources.getString(it.first), get(it.first) + it.second)
            }
            apply()
        }
    }

    private fun get(strId: Int, defValue: Long = 0L): Long =
        prefs.getLong(context.resources.getString(strId), defValue)

    private fun getInt(strId: Int, defValue: Int) =
        prefs.getInt(context.resources.getString(strId), defValue)

    private fun getBoolean(strId: Int, defValue: Boolean) =
        prefs.getBoolean(context.resources.getString(strId), defValue)

    private fun onFirstStartUp() {
        putLongs(
            Pair(touchesTotalId, 0L),
            Pair(attemptsOneTotalId, 0L),
            Pair(attemptsQueueTotalId, 0L),
        )
        isFirstTime = false
    }

    override fun onStartUp() {
        if (isFirstTime) onFirstStartUp()
        putLongs(
            Pair(touchesCurrentId, 0L),
            Pair(attemptsOneCurrentId, 0L),
            Pair(attemptsQueueCurrentId, 0L),
        )
    }

    override fun increaseTouches(value: Long) {
        increaseLongs(
            Pair(touchesCurrentId, value),
            Pair(touchesTotalId, value),
        )
    }

    override fun increaseAttemptsOne(value: Long) {
        increaseLongs(
            Pair(attemptsOneCurrentId, value),
            Pair(attemptsOneTotalId, value),
        )
    }

    override fun increaseAttemptsQueue(value: Long) {
        increaseLongs(
            Pair(attemptsQueueCurrentId, value),
            Pair(attemptsQueueTotalId, value),
        )
    }
}