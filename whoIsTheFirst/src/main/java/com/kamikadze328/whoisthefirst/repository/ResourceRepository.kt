package com.kamikadze328.whoisthefirst.repository

import android.content.Context
import com.kamikadze328.whoisthefirst.R

class ResourceRepository(private val context: Context) {
    private var colors: MutableList<Int> = mutableListOf()

    fun getColors(): List<Int> {
        if (colors.isEmpty()) loadColors()
        return colors.shuffled()
    }

    private fun loadColors() =
        context.resources.getIntArray(R.array.circle_colors).forEach { colors.add(it) }
}