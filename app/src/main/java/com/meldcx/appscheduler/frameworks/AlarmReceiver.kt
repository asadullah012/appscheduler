package com.meldcx.appscheduler.frameworks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.getStringExtra("PACKAGE_NAME") ?: return
        val scheduleId = intent.getIntExtra("SCHEDULE_ID", -1)
        Log.d("AlarmReceiver", "onReceive: $packageName $scheduleId")

        startAppLaunchService(context, packageName)

//        val packageName = intent.getStringExtra("PACKAGE_NAME") ?: return
//        val scheduleId = intent.getIntExtra("SCHEDULE_ID", -1)
//        Log.d("AlarmReceiver", "onReceive: $packageName $scheduleId")
//
//        val launchScheduleUseCase: LaunchScheduleUseCase = GlobalContext.get().get()
//
//        // Fetch schedule data synchronously
////        val launchSchedule: LaunchSchedule? = runBlocking {
////            launchScheduleUseCase.getByScheduleId(scheduleId).firstOrNull()
////        }
//
//        // Launch the app
//        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
//        if (launchIntent != null) {
//            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(launchIntent)
//            //launchSchedule?.status = SCHEDULE_STATUS.EXECUTED
//        } else {
//        //    launchSchedule?.status = SCHEDULE_STATUS.FAILED_DUE_TO_APP_UNINSTALLED
//        }

        // Update schedule status in Room DB
//        launchSchedule?.let {
//            CoroutineScope(Dispatchers.IO).launch {
//                launchScheduleUseCase.update(it)
//            }
//        }
    }
}