package com.meldcx.appscheduler.domain.usecase

import android.content.Context
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.domain.repository.ScheduleRepository
import com.meldcx.appscheduler.frameworks.cancelAlarm
import com.meldcx.appscheduler.frameworks.setAlarm
import com.meldcx.appscheduler.frameworks.updateAlarm
import kotlinx.coroutines.flow.Flow

class LaunchScheduleUseCase(
    private val context: Context,
    private val scheduleRepository: ScheduleRepository
) {
    suspend fun schedule(launchSchedule: LaunchSchedule) {
        val scheduleId = scheduleRepository.scheduleAppLaunch(launchSchedule)
        val updatedLaunchSchedule = launchSchedule.copy(scheduleId = scheduleId)
        setAlarm(context, updatedLaunchSchedule)
    }

    suspend fun update(launchSchedule: LaunchSchedule) {
        scheduleRepository.updateLaunchSchedule(launchSchedule)
        if(launchSchedule.status == SCHEDULE_STATUS.SCHEDULED) {
            updateAlarm(context, launchSchedule)
        }
    }

    suspend fun cancel(launchSchedule: LaunchSchedule) {
        scheduleRepository.cancelLaunchSchedule(launchSchedule)
        cancelAlarm(context, launchSchedule)
    }

    fun getByScheduleId(scheduleId: Int): Flow<LaunchSchedule> {
        return scheduleRepository.getLaunchSchedulesByScheduleId(scheduleId)
    }

    fun getAll(): Flow<List<LaunchSchedule>> {
        return scheduleRepository.getAllLaunchSchedules()
    }

    fun getScheduledAppsByStatus(status: SCHEDULE_STATUS): Flow<List<LaunchSchedule>> {
        return scheduleRepository.getScheduleByStatus(status)
    }

    suspend fun deleteByScheduleId(scheduleId: Int){
        return scheduleRepository.deleteLaunchSchedulesByScheduleId(scheduleId)
    }

    suspend fun deleteAllSchedule(){
        return scheduleRepository.deleteAllLaunchSchedules()
    }
}