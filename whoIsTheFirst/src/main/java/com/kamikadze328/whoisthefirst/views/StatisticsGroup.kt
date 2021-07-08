package com.kamikadze328.whoisthefirst.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.kamikadze328.whoisthefirst.R
import com.kamikadze328.whoisthefirst.auxiliary_classes.MyPreferencesManager

class StatisticsGroup(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {


    init {
        inflate(context, R.layout.statistics_group_view, this)

        val textViewKey: TextView = findViewById(R.id.groupName)
        val touchesView: StatisticsRow = findViewById(R.id.statisticRowTouches)
        val attemptsView: StatisticsRow = findViewById(R.id.statisticRowAttempts)
        val attemptsOneView: StatisticsRow = findViewById(R.id.statisticRowAttemptsOne)
        val attemptsQueueView: StatisticsRow = findViewById(R.id.statisticRowAttemptsQueue)

        val a = context.obtainStyledAttributes(attrs, R.styleable.StatisticsGroup)
        val groupName = a.getString(R.styleable.StatisticsGroup_name)
        textViewKey.text = groupName

        val isTotal = groupName == context.resources.getString(R.string.total)

        val pref = MyPreferencesManager(context)

        touchesView.value = if (isTotal) pref.touchesTotal else pref.touchesCurrent
        attemptsView.value = if (isTotal) pref.attemptsTotal else pref.attemptsCurrent
        attemptsOneView.value = if (isTotal) pref.attemptsOneTotal else pref.attemptsOneCurrent
        attemptsQueueView.value =
            if (isTotal) pref.attemptsQueueTotal else pref.attemptsQueueCurrent

        a.recycle()
    }
}