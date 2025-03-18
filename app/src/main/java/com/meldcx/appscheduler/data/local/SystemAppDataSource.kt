package com.meldcx.appscheduler.data.local

import android.content.Intent
import android.content.pm.PackageManager
import com.meldcx.appscheduler.data.model.AppEntity

class SystemAppDataSource(
    private val packageManager: PackageManager
) {
    fun fetchInstalledApps(): List<AppEntity> {
        val packages = packageManager.getInstalledPackages(0)
        return packages.map { packageInfo ->
            AppEntity(
                packageName = packageInfo.packageName,
                appName = packageInfo.applicationInfo?.loadLabel(packageManager).toString(),
                versionName = packageInfo.versionName ?: "Unknown"
            )
        }
    }

    fun fetchLauncherInstalledApps(): List<AppEntity> {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfo = packageManager.queryIntentActivities(intent, 0)
        return resolveInfo.map {
            AppEntity(
                packageName = it.activityInfo.packageName,
                appName = it.loadLabel(packageManager).toString(),
                versionName = "Unknown"
            )
        }
    }
}