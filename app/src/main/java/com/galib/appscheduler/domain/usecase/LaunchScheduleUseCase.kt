package com.galib.appscheduler.domain.usecase

import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleResult
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.domain.repository.ScheduleRepository
import com.galib.appscheduler.domain.scheduler.AlarmScheduler
import kotlinx.coroutines.flow.Flow

class LaunchScheduleUseCase(
    private val alarmScheduler: AlarmScheduler,
    private val scheduleRepository: ScheduleRepository
) {
    suspend fun schedule(launchSchedule: LaunchSchedule): ScheduleResult {
        if (launchSchedule.scheduledTime <= System.currentTimeMillis()) {
            return ScheduleResult.PastTimeError
        }
        val conflicting = scheduleRepository.findConflictingSchedule(launchSchedule.scheduledTime)
        if (conflicting != null) {
            return ScheduleResult.Conflict(conflicting)
        }
        val scheduleId = scheduleRepository.scheduleAppLaunch(launchSchedule)
        val updatedLaunchSchedule = launchSchedule.copy(scheduleId = scheduleId)
        alarmScheduler.schedule(updatedLaunchSchedule)
        return ScheduleResult.Success(scheduleId)
    }

    suspend fun update(launchSchedule: LaunchSchedule): ScheduleResult {
        if (launchSchedule.status == ScheduleStatus.SCHEDULED) {
            if (launchSchedule.scheduledTime <= System.currentTimeMillis()) {
                return ScheduleResult.PastTimeError
            }
            val conflicting = scheduleRepository.findConflictingSchedule(
                time = launchSchedule.scheduledTime,
                excludeScheduleId = launchSchedule.scheduleId
            )
            if (conflicting != null) {
                return ScheduleResult.Conflict(conflicting)
            }
            alarmScheduler.update(launchSchedule)
        }
        scheduleRepository.updateLaunchSchedule(launchSchedule)
        return ScheduleResult.Success(launchSchedule.scheduleId)
    }

    suspend fun cancel(launchSchedule: LaunchSchedule) {
        scheduleRepository.cancelLaunchSchedule(launchSchedule)
        alarmScheduler.cancel(launchSchedule)
    }

    fun getByScheduleId(scheduleId: Int): Flow<LaunchSchedule> {
        return scheduleRepository.getLaunchSchedulesByScheduleId(scheduleId)
    }

    fun getAll(): Flow<List<LaunchSchedule>> {
        return scheduleRepository.getAllLaunchSchedules()
    }

    fun getScheduledAppsByStatus(status: ScheduleStatus): Flow<List<LaunchSchedule>> {
        return scheduleRepository.getScheduleByStatus(status)
    }

    suspend fun deleteByScheduleId(scheduleId: Int) {
        scheduleRepository.deleteLaunchSchedulesByScheduleId(scheduleId)
    }

    suspend fun deleteAllSchedule() {
        scheduleRepository.deleteAllLaunchSchedules()
    }
}