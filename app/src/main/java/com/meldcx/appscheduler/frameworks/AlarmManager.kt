package com.meldcx.appscheduler.frameworks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.meldcx.appscheduler.domain.model.LaunchSchedule

fun setAlarm(context: Context, launchSchedule: LaunchSchedule) {
    Log.d("TAG", "setAlarm: setting alarm for ${launchSchedule.appName}")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("PACKAGE_NAME", launchSchedule.packageName)
        putExtra("SCHEDULE_ID", launchSchedule.scheduleId)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context, launchSchedule.scheduleId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        if(alarmManager.canScheduleExactAlarms()){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, launchSchedule.scheduledTime, pendingIntent)
        } else {
            Log.e("AlarmManager", "setAlarm: can not schedule exact alarm")
        }
    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, launchSchedule.scheduledTime, pendingIntent)
    }

}

fun updateAlarm(context: Context, launchSchedule: LaunchSchedule) {
    cancelAlarm(context, launchSchedule)
    setAlarm(context, launchSchedule)
}

fun cancelAlarm(context: Context, launchSchedule: LaunchSchedule) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context, launchSchedule.scheduleId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
}

