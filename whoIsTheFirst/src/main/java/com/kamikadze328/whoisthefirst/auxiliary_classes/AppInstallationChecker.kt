package com.kamikadze328.whoisthefirst.auxiliary_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.huawei.hms.jos.AppUpdateClient
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey
import com.kamikadze328.whoisthefirst.auxiliary_classes.ActivityUtils.getSerializable


enum class InstallSource(val sourceIds: List<String> = emptyList()) {
    GOOGLE(listOf("com.android.vending", "com.google.android.feedback")),
    HUAWEI(listOf("com.huawei.appmarket")),
    RU_STORE(listOf("ru.vk.store")),
    UNKNOWN
}

fun checkUpdates(activity: Activity) {
    when (verifyInstallerId(activity)) {
        InstallSource.GOOGLE -> checkUpdatesGooglePlay(activity)
        InstallSource.HUAWEI -> checkUpdatesHuawei(activity)
        else -> {
        }
    }
}


fun checkUpdatesGooglePlay(activity: Activity) {
    val appUpdateManager = AppUpdateManagerFactory.create(activity)

    // Returns an intent object that you use to check for an update.
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo

    // Checks that the platform will allow the specified type of update.
    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
            // This example applies an immediate update. To apply a flexible update
            // instead, pass in AppUpdateType.FLEXIBLE
            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
        ) {

            appUpdateManager.startUpdateFlowForResult(
                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                appUpdateInfo,
                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                AppUpdateType.IMMEDIATE,
                // The current activity making the update request.
                activity,
                // Include a request code to later monitor this update request.
                1
            )
        }
    }
}

fun checkUpdatesHuawei(activity: Activity) {
    val client: AppUpdateClient = JosApps.getAppUpdateClient(activity)
    client.checkAppUpdate(activity, object : CheckUpdateCallBack {
        override fun onUpdateInfo(intent: Intent) {
            val info: ApkUpgradeInfo? = intent.getSerializable(UpdateKey.INFO)
            if (info != null) {
                client.showUpdateDialog(activity, info, true)
            }
        }

        override fun onMarketInstallInfo(intent: Intent) {}
        override fun onMarketStoreError(i: Int) {}
        override fun onUpdateStoreError(i: Int) {}
    })
}


fun verifyInstallerId(context: Context): InstallSource {
    // The package name of the app that has installed your app
    val installer = getInstallerStr(context)

    return InstallSource.values().firstOrNull {
        it.sourceIds.contains(installer)
    } ?: InstallSource.UNKNOWN
}

private fun getInstallerStr(context: Context): String? {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName
    } else {
        @Suppress("DEPRECATION") context.packageManager.getInstallerPackageName(context.packageName)
    }
}