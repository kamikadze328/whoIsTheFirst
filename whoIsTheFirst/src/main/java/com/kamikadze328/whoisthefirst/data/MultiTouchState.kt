package com.kamikadze328.whoisthefirst.data

import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kamikadze328.whoisthefirst.R

enum class TextSize(@DimenRes val dimenId: Int) {
    BIG(R.dimen.text_description_big_size),
    BIG_NORMAL(R.dimen.big_normal),
    NORMAL(R.dimen.text_description_one_of_size)
}

enum class MultiTouchState(@StringRes val textRes: Int, val textSize: TextSize, var payload: Any?) {
    DEFAULT(R.string.helpText, TextSize.NORMAL, null),
    YOU_ARE_ALONE(R.string.youAreOnlyTheFirst, TextSize.BIG_NORMAL, null),
    YOU_ARE_ALONE_TIMER(DEFAULT.textRes, DEFAULT.textSize, DEFAULT.payload),
    FINISH_BUT_WINNER_POINTER_IS_DOWN(R.string.youWin, TextSize.BIG_NORMAL, null),
    FINISH(R.string.helpStartAgain, TextSize.BIG_NORMAL, null),
    TIMER(0, TextSize.BIG, 0L)
}

enum class Mode(@DrawableRes val imageId: Int) {
    ONE(R.drawable.ic_first_50),
    QUEUE(R.drawable.ic_queue_50),
    ONLINE_ONE(R.drawable.ic_first_50),
    ONLINE_QUEUE(R.drawable.ic_queue_50)
}

const val TIMEOUT_MS_ALONE = 2500L