package com.meldcx.appscheduler.domain.model

enum class SCHEDULE_STATUS {
    SCHEDULED,
    EXECUTED,
    CANCELLED
}

data class LaunchSchedule(
    val scheduleId: Long = 0L,
    val packageName: String,
    val scheduledTime: Long,
    val status: SCHEDULE_STATUS
)