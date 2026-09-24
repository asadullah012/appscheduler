package com.galib.appscheduler.frameworks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.domain.scheduler.AlarmScheduler
import com.galib.appscheduler.utils.formatTimestamp

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(launchSchedule: LaunchSchedule): Boolean {
        if (launchSchedule.status != ScheduleStatus.SCHEDULED) {
            Log.e(TAG, "schedule: No need to schedule status: ${launchSchedule.status}")
            return false
        }

        Log.d(TAG, "schedule: Setting exact alarm for ${launchSchedule.appName} at ${formatTimestamp(launchSchedule.scheduledTime)}")
        val pendingIntent = createPendingIntent(launchSchedule)

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        launchSchedule.scheduledTime,
                        pendingIntent
                    )
                    true
                } else {
                    Log.w(TAG, "schedule: canScheduleExactAlarms is false. Falling back to inexact idle alarm.")
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        launchSchedule.scheduledTime,
                        pendingIntent
                    )
                    false
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    launchSchedule.scheduledTime,
                    pendingIntent
                )
                true
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException while scheduling exact alarm", e)
            false
        }
    }

    override fun update(launchSchedule: LaunchSchedule): Boolean {
        Log.d(TAG, "update: Updating alarm for ${launchSchedule.appName}")
        cancel(launchSchedule)
        return schedule(launchSchedule)
    }

    override fun cancel(launchSchedule: LaunchSchedule) {
        Log.d(TAG, "cancel: Canceling alarm for ${launchSchedule.appName}")
        val pendingIntent = createPendingIntent(launchSchedule)
        alarmManager.cancel(pendingIntent)
    }

    override fun isAlarmScheduled(scheduleId: Int): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduleId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent != null
    }

    private fun createPendingIntent(launchSchedule: LaunchSchedule): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("PACKAGE_NAME", launchSchedule.packageName)
            putExtra("SCHEDULE_ID", launchSchedule.scheduleId)
        }
        return PendingIntent.getBroadcast(
            context,
            launchSchedule.scheduleId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val TAG = "AlarmScheduler"
    }
}
