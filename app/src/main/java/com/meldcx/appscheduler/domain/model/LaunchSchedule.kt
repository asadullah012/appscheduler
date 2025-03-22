package com.meldcx.appscheduler.domain.model

enum class SCHEDULE_STATUS(val order: Int) {
    SCHEDULED(1),
    EXECUTED(2),
    CANCELLED_BY_USER(3),
    FAILED_DUE_TO_APP_UNINSTALLED(4);
}

data class LaunchSchedule(
    var scheduleId: Int = 0,
    var packageName: String,
    var appName: String,
    var scheduledTime: Long,
    var status: SCHEDULE_STATUS
)