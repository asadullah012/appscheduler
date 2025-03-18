package com.galib.appscheduler.data.local

import android.content.pm.PackageManager
import com.galib.appscheduler.data.model.AppEntity

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
}