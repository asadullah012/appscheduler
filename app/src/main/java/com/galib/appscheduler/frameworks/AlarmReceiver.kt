package com.galib.appscheduler.frameworks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.domain.usecase.LaunchScheduleUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.getStringExtra("PACKAGE_NAME") ?: return
        val scheduleId = intent.getIntExtra("SCHEDULE_ID", -1)
        Log.d("AlarmReceiver", "onReceive: $packageName $scheduleId")

        val pendingResult = goAsync()
        val launchScheduleUseCase: LaunchScheduleUseCase = GlobalContext.get().get()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val launchSchedule: LaunchSchedule? = launchScheduleUseCase.getByScheduleId(scheduleId).firstOrNull()
                withContext(Dispatchers.Main) {
                    val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(launchIntent)
                        launchSchedule?.status = ScheduleStatus.EXECUTED
                    } else {
                        launchSchedule?.status = ScheduleStatus.FAILED_DUE_TO_APP_UNINSTALLED
                    }

                    launchSchedule?.let {
                        withContext(Dispatchers.IO) {
                            launchScheduleUseCase.update(it)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AlarmReceiver", "Error handling scheduled alarm", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}