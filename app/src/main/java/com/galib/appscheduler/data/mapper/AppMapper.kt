package com.galib.appscheduler.data.mapper

import com.galib.appscheduler.data.model.AppEntity
import com.galib.appscheduler.domain.model.AppInfo

fun AppEntity.toDomain(): AppInfo {
    return AppInfo(
        packageName = this.packageName,
        appName = this.appName,
        versionName = this.versionName
    )
}

fun AppInfo.toEntity(): AppEntity {
    return AppEntity(
        packageName = this.packageName,
        appName = this.appName,
        versionName = this.versionName
    )
}