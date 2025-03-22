package com.meldcx.appscheduler.frameworks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.domain.usecase.LaunchScheduleUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.getStringExtra("PACKAGE_NAME") ?: return
        val scheduleId = intent.getIntExtra("SCHEDULE_ID", -1)
        Log.d("AlarmReceiver", "onReceive: $packageName $scheduleId")

        val launchScheduleUseCase: LaunchScheduleUseCase = GlobalContext.get().get()

        val launchSchedule: LaunchSchedule? = runBlocking {
            launchScheduleUseCase.getByScheduleId(scheduleId).firstOrNull()
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            launchSchedule?.status = SCHEDULE_STATUS.EXECUTED
        } else {
            launchSchedule?.status = SCHEDULE_STATUS.FAILED_DUE_TO_APP_UNINSTALLED
        }

        launchSchedule?.let {
            CoroutineScope(Dispatchers.IO).launch {
                launchScheduleUseCase.update(it)
            }
        }
    }
}