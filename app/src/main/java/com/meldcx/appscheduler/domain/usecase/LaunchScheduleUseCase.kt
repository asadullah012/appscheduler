package com.meldcx.appscheduler.domain.usecase

import android.content.Context
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.repository.ScheduleRepository
import com.meldcx.appscheduler.frameworks.setAlarm
import kotlinx.coroutines.flow.Flow

class LaunchScheduleUseCase(
    private val context: Context,
    private val scheduleRepository: ScheduleRepository
) {
    suspend  fun schedule(launchSchedule: LaunchSchedule) {
        scheduleRepository.scheduleAppLaunch(launchSchedule)
        setAlarm(context, launchSchedule)
    }

    suspend fun update(launchSchedule: LaunchSchedule) {
        scheduleRepository.updateLaunchSchedule(launchSchedule)
    }

    suspend fun cancel(launchSchedule: LaunchSchedule) {
        scheduleRepository.cancelLaunchSchedule(launchSchedule)
    }

    fun getByScheduleId(scheduleId: Int): Flow<LaunchSchedule> {
        return scheduleRepository.getLaunchSchedulesByScheduleId(scheduleId)
    }

    fun getAll(): Flow<List<LaunchSchedule>> {
        return scheduleRepository.getAllLaunchSchedules()
    }

    suspend fun deleteByScheduleId(scheduleId: Int){
        return scheduleRepository.deleteLaunchSchedulesByScheduleId(scheduleId)
    }

    suspend fun deleteAllSchedule(){
        return scheduleRepository.deleteAllLaunchSchedules()
    }
}