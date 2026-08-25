package com.kamikadze328.whoisthefirst.auxiliary_classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey


enum class InstallSource(val sourceIds: List<String> = emptyList()) {
    GOOGLE(listOf("com.android.vending", "com.google.android.feedback")),
    HUAWEI(listOf("com.huawei.appmarket")),
    RU_STORE(listOf("ru.vk.store")),
    UNKNOWN
}

fun Activity.checkUpdates(updateLauncher: ActivityResultLauncher<IntentSenderRequest>) {
    when (verifyInstallerId()) {
        InstallSource.GOOGLE -> checkUpdatesGooglePlay(updateLauncher)
        InstallSource.HUAWEI -> checkUpdatesHuawei()
        InstallSource.RU_STORE,
        InstallSource.UNKNOWN -> Unit
    }
}


fun Activity.checkUpdatesGooglePlay(updateLauncher: ActivityResultLauncher<IntentSenderRequest>) {
    val appUpdateManager = AppUpdateManagerFactory.create(this)
    val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()

    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
        ) {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                updateLauncher,
                updateOptions,
            )
        }
    }
}

fun Activity.checkUpdatesHuawei() {
    val client = JosApps.getAppUpdateClient(this)
    client.checkAppUpdate(this, object : CheckUpdateCallBack {
        override fun onUpdateInfo(intent: Intent) {
            val info: ApkUpgradeInfo? = intent.getSerializable(UpdateKey.INFO)
            if (info != null) {
                client.showUpdateDialog(this@checkUpdatesHuawei, info, true)
            }
        }

        override fun onMarketInstallInfo(intent: Intent) {}
        override fun onMarketStoreError(i: Int) {}
        override fun onUpdateStoreError(i: Int) {}
    })
}


fun Context.verifyInstallerId(): InstallSource {
    // The package name of the app that has installed your app
    val installer = getInstallerStr()

    return InstallSource.entries.firstOrNull { it.sourceIds.contains(installer) } ?: InstallSource.UNKNOWN
}

private fun Context.getInstallerStr(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        packageManager.getInstallSourceInfo(packageName).installingPackageName
    } else {
        @Suppress("DEPRECATION") packageManager.getInstallerPackageName(packageName)
    }
}