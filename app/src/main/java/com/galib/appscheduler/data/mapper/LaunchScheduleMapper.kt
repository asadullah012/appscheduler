package com.galib.appscheduler.data.mapper

import com.galib.appscheduler.data.model.LaunchScheduleEntity
import com.galib.appscheduler.domain.model.LaunchSchedule


fun LaunchScheduleEntity.toDomain(): LaunchSchedule {
    return LaunchSchedule(
        scheduleId = this.scheduleId,
        packageName = this.packageName,
        scheduledTime = this.scheduledTime,
        status = this.status
    )
}

fun LaunchSchedule.toEntity(): LaunchScheduleEntity {
    return LaunchScheduleEntity(
        scheduleId = this.scheduleId,
        packageName = this.packageName,
        scheduledTime = this.scheduledTime,
        status = this.status
    )
}
