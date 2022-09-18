package com.kamikadze328.whoisthefirst.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.kamikadze328.whoisthefirst.R

class StatisticsGroup(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val textViewKey: TextView by lazy { findViewById(R.id.groupName) }
    private val touchesView: StatisticsRow by lazy { findViewById(R.id.statisticRowTouches) }
    private val attemptsView: StatisticsRow by lazy { findViewById(R.id.statisticRowAttempts) }
    private val attemptsOneView: StatisticsRow by lazy { findViewById(R.id.statisticRowAttemptsOne) }
    private val attemptsQueueView: StatisticsRow by lazy { findViewById(R.id.statisticRowAttemptsQueue) }

    init {
        inflate(context, R.layout.custom_view_statistics_group, this)

        context.obtainStyledAttributes(attrs, R.styleable.StatisticsGroup).apply {
            val groupName = getString(R.styleable.StatisticsGroup_name)
            recycle()
            textViewKey.text = groupName
        }
    }

    fun setTouchesValue(value: Long) {
        touchesView.value = value
    }

    fun setAttemptsValue(value: Long) {
        attemptsView.value = value
    }

    fun setAttemptsOneValue(value: Long) {
        attemptsOneView.value = value
    }

    fun setAttemptsQueueValue(value: Long) {
        attemptsQueueView.value = value
    }


}