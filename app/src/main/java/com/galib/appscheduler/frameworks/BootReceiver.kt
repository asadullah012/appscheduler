package com.galib.appscheduler.frameworks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.domain.usecase.LaunchScheduleUseCase
import com.galib.appscheduler.utils.formatTimestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            Log.d("BootReceiver", "onReceive: checking for scheduled apps")
            val pendingResult = goAsync()
            val launchScheduleUseCase: LaunchScheduleUseCase = GlobalContext.get().get()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val launchSchedules: List<LaunchSchedule> = launchScheduleUseCase.getScheduledAppsByStatus(ScheduleStatus.SCHEDULED).first()
                    Log.d("BootReceiver", "onReceive: ${launchSchedules.size}")
                    withContext(Dispatchers.Main){
                        launchSchedules.forEach {
                            Log.d("BootReceiver", "Checking for alarm: ${it.appName} at ${formatTimestamp(it.scheduledTime)}")
                            if(it.scheduledTime < System.currentTimeMillis()) {
                                Log.e("BootReceiver", "setAlarm: scheduled time is in the past")
                                withContext(Dispatchers.IO) {
                                    launchScheduleUseCase.update(it.copy(status = ScheduleStatus.FAILED_DUE_TO_DEVICE_TURNED_OFF))
                                }
                            } else {
                                if(!isAlarmScheduled(context, it.scheduleId)){
                                    setAlarm(context, it)
                                } else {
                                    Log.d("BootReceiver", "setAlarm: alarm is already scheduled")
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("BootReceiver", "Error restoring alarms on boot", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
