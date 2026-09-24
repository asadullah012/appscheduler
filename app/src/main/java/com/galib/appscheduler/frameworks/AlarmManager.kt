package com.galib.appscheduler.frameworks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.utils.formatTimestamp

fun setAlarm(context: Context, launchSchedule: LaunchSchedule) {
    AndroidAlarmScheduler(context).schedule(launchSchedule)
}

fun updateAlarm(context: Context, launchSchedule: LaunchSchedule) {
    AndroidAlarmScheduler(context).update(launchSchedule)
}

fun cancelAlarm(context: Context, launchSchedule: LaunchSchedule) {
    AndroidAlarmScheduler(context).cancel(launchSchedule)
}

fun isAlarmScheduled(context: Context, scheduleId: Int): Boolean {
    return AndroidAlarmScheduler(context).isAlarmScheduled(scheduleId)
}
