package com.meldcx.appscheduler.domain.model

enum class SCHEDULE_STATUS {
    SCHEDULED,
    EXECUTED,
    CANCELLED_BY_USER,
    FAILED_DUE_TO_APP_UNINSTALLED
}

data class LaunchSchedule(
    var scheduleId: Int = 0,
    var packageName: String,
    var appName: String,
    var scheduledTime: Long,
    var status: SCHEDULE_STATUS
)