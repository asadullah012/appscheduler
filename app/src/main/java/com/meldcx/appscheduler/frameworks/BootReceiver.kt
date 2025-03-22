package com.meldcx.appscheduler.frameworks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.domain.usecase.LaunchScheduleUseCase
import com.meldcx.appscheduler.utils.formatTimestamp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("TAG", "onReceive: checking for scheduled apps")
            val launchScheduleUseCase: LaunchScheduleUseCase = GlobalContext.get().get()
            val launchSchedule: List<LaunchSchedule> = runBlocking {
                launchScheduleUseCase.getScheduledAppsByStatus(SCHEDULE_STATUS.SCHEDULED).first()
            }
            launchSchedule.forEach {
                Log.d("TAG", "onReceive: ${it.appName} at ${formatTimestamp(it.scheduledTime)}")
               if(!isAlarmScheduled(context, it.scheduleId)){
                   setAlarm(context, it)
               }
            }
        }
    }
}
