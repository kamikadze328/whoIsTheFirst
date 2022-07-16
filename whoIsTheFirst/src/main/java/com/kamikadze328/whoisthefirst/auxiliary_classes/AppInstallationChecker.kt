package com.kamikadze328.whoisthefirst.auxiliary_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.huawei.hms.jos.AppUpdateClient
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey


enum class InstallFrom { GOOGLE, HUAWEI, UNKNOWN }

fun checkUpdates(activity: Activity) {
    when (verifyInstallerId(activity)) {
        InstallFrom.GOOGLE -> checkUpdatesGooglePlay(activity)
        InstallFrom.HUAWEI -> checkUpdatesHuawei(activity)
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
            val status = intent.getIntExtra(UpdateKey.STATUS, -1)
            val rtnCode = intent.getIntExtra(UpdateKey.FAIL_CODE, -1)
            val rtnMessage = intent.getStringExtra(UpdateKey.FAIL_REASON)
            val info = intent.getSerializableExtra(UpdateKey.INFO)
            if (info is ApkUpgradeInfo) {
                Log.v("kek", "There is a new update")
                client.showUpdateDialog(activity, info, true)
            }
            Log.v(
                "kek",
                "onUpdateInfo status: $status, rtnCode: $rtnCode, rtnMessage: $rtnMessage"
            )
        }

        override fun onMarketInstallInfo(intent: Intent) {}
        override fun onMarketStoreError(i: Int) {}
        override fun onUpdateStoreError(i: Int) {}
    })
}


fun verifyInstallerId(context: Context): InstallFrom {
    // A list with valid installers package name
    val googleStoreIds = listOf("com.android.vending", "com.google.android.feedback")
    val huaweiStoreIds = listOf("com.huawei.appmarket")
    // The package name of the app that has installed your app
    val installer: String? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) installerStrAPILess30(context) else installerStrAPI30(
            context
        )

    if (installer != null) {
        Log.v("kek", installer)
    }
    return when {
        installer == null -> InstallFrom.UNKNOWN
        googleStoreIds.contains(installer) -> InstallFrom.GOOGLE
        huaweiStoreIds.contains(installer) -> InstallFrom.HUAWEI
        else -> InstallFrom.UNKNOWN
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun installerStrAPI30(context: Context) =
    context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName

@Suppress("DEPRECATION")
private fun installerStrAPILess30(context: Context) =
    context.packageManager.getInstallerPackageName(context.packageName)