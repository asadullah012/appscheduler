package com.meldcx.appscheduler.data.mapper

import com.meldcx.appscheduler.data.model.LaunchScheduleEntity
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS


fun LaunchScheduleEntity.toDomain(): LaunchSchedule {
    return LaunchSchedule(
        scheduleId = this.scheduleId,
        packageName = this.packageName,
        appName = this.appName,
        scheduledTime = this.scheduledTime,
        status = enumValues<SCHEDULE_STATUS>()[this.status]
    )
}

fun LaunchSchedule.toEntity(): LaunchScheduleEntity {
    return LaunchScheduleEntity(
        scheduleId = this.scheduleId,
        packageName = this.packageName,
        appName = this.appName,
        scheduledTime = this.scheduledTime,
        status = this.status.ordinal
    )
}
