package com.galib.appscheduler.domain.scheduler

import com.galib.appscheduler.domain.model.LaunchSchedule

/**
 * Domain-level abstraction for scheduling alarms.
 * Keeps the domain layer decoupled from the Android framework.
 */
interface AlarmScheduler {
    fun schedule(launchSchedule: LaunchSchedule): Boolean
    fun update(launchSchedule: LaunchSchedule): Boolean
    fun cancel(launchSchedule: LaunchSchedule)
    fun isAlarmScheduled(scheduleId: Int): Boolean
}
