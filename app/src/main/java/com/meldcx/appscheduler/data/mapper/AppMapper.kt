package com.meldcx.appscheduler.data.mapper

import com.meldcx.appscheduler.data.model.AppEntity
import com.meldcx.appscheduler.domain.model.AppInfo

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