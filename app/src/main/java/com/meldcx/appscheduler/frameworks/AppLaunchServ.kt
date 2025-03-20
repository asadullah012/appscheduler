package com.meldcx.appscheduler.frameworks

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class AppLaunchServ : Service() {

    override fun onCreate() {
        super.onCreate()
        setupForegroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopSelf()
            }
            ACTION_LAUNCH_APP -> {
                val packageName = intent.getStringExtra("PACKAGE_NAME")
                packageName?.let {
                    launchApp(it)
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AppLaunchService", "Service Destroyed")
    }

    private fun setupForegroundNotification() {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        val stopIntent = Intent(this, AppLaunchServ::class.java).apply {
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
            .setContentText("Preparing to launch scheduled app")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_media_pause, "Stop", stopPendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "App Launch Service", NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun launchApp(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
            Log.d("AppLaunchService", "App launched: $packageName")
        } else {
            Log.e("AppLaunchService", "App not found: $packageName")
        }
    }

    override fun onBind(intent: Intent?) = null

    companion object {
        const val CHANNEL_ID = "AppLaunchServiceChannel"
        const val NOTIFICATION_ID = 1
        const val ACTION_STOP = "action_stop"
        const val ACTION_LAUNCH_APP = "action_launch_app"
    }
}

// Function to start the service
fun startAppLaunchService(context: Context, packageName: String?) {
    Log.d("AppLaunchService", "Starting service to launch: $packageName")
    val intent = Intent(context, AppLaunchServ::class.java).apply {
        action = AppLaunchServ.ACTION_LAUNCH_APP
        putExtra("PACKAGE_NAME", packageName)
    }
    ContextCompat.startForegroundService(context, intent)
}
