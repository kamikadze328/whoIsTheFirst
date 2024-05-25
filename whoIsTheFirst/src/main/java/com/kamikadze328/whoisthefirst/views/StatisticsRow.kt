package com.kamikadze328.whoisthefirst.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.kamikadze328.whoisthefirst.R

class StatisticsRow(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    var value: Long = 0L
        set(value) {
            field = value
            this.reDraw()
        }

    init {
        inflate(context, R.layout.custom_view_statistics_row, this)

        val a = context.obtainStyledAttributes(attrs, R.styleable.StatisticsRow)

        findViewById<TextView>(R.id.statisticsRowKey).text =
            a.getString(R.styleable.StatisticsRow_key)

        a.recycle()
    }

    private fun reDraw() {
        findViewById<TextView>(R.id.statisticsRowValue).text = value.toString()
    }
}