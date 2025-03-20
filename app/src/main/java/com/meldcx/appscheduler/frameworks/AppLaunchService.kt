package com.meldcx.appscheduler.frameworks

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService


class AppLaunchService : LifecycleService() {

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate() {
        super.onCreate()
        setupForegroundNotification()
        registerAlarmReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterAlarmReceiver()
    }

    private fun setupForegroundNotification() {
        createNotificationChannel()
        val notification = createNotification()
        notification.flags = Notification.FLAG_ONGOING_EVENT
        startForeground(NOTIFICATION_ID, notification)
    }
    // Ongoing Issue https://developer.android.com/about/versions/14/behavior-changes-all#non-dismissable-notifications
    private fun createNotification(): Notification {
        val stopIntent = Intent(this, AppLaunchService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App Launch Service")
            .setContentText("Running in background to launch a scheduled app launch")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_media_pause, "Stop", stopPendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        val channelName = "App Launch Service"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
        val notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun registerAlarmReceiver() {
        alarmReceiver = AlarmReceiver()
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(alarmReceiver, intentFilter)
    }

    private fun unregisterAlarmReceiver() {
        unregisterReceiver(alarmReceiver)
    }

    companion object {
        const val CHANNEL_ID = "AppLaunchServiceChannel"
        const val NOTIFICATION_ID = 1
        const val ACTION_STOP = "action_stop"
    }
}

fun startAppLaunchServic(context: Context) {
    Log.d("TAG", "startAppLaunchService")
    val intent = Intent(context, AppLaunchService::class.java)
    ContextCompat.startForegroundService(context, intent)
}

fun stopAppLaunchService(context: Context) {
    val intent = Intent(context, AppLaunchService::class.java)
    context.stopService(intent)
}