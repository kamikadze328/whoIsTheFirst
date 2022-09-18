package com.kamikadze328.whoisthefirst.auxiliary_classes

import android.content.Intent
import android.os.Build
import java.io.Serializable

object ActivityUtils {
    inline fun <reified T : Serializable> Intent.getSerializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") (getSerializableExtra(key) as T?)
    }
}