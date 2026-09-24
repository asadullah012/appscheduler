package com.galib.appscheduler.domain.model

sealed interface ScheduleResult {
    data class Success(val scheduleId: Int) : ScheduleResult
    data class Conflict(val conflictingSchedule: LaunchSchedule) : ScheduleResult
    data object PastTimeError : ScheduleResult
    data class Failure(val message: String) : ScheduleResult
}
