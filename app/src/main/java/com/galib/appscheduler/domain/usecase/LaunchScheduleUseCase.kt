package com.galib.appscheduler.domain.usecase

import android.content.Context
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.SCHEDULE_STATUS
import com.galib.appscheduler.domain.repository.ScheduleRepository
import com.galib.appscheduler.frameworks.cancelAlarm
import com.galib.appscheduler.frameworks.setAlarm
import com.galib.appscheduler.frameworks.updateAlarm
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
        updateAlarm(context, launchSchedule)
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