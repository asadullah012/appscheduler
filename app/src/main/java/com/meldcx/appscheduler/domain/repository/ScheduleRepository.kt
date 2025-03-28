package com.meldcx.appscheduler.domain.repository

import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.ScheduleStatus
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    suspend fun scheduleAppLaunch(launchSchedule: LaunchSchedule):Int
    suspend fun updateLaunchSchedule(launchSchedule: LaunchSchedule)
    suspend fun cancelLaunchSchedule(launchSchedule: LaunchSchedule)
    fun getLaunchSchedulesByScheduleId(scheduleId: Int): Flow<LaunchSchedule>
    fun getAllLaunchSchedules(): Flow<List<LaunchSchedule>>
    fun getScheduleByStatus(status: ScheduleStatus): Flow<List<LaunchSchedule>>
    suspend fun deleteLaunchSchedulesByScheduleId(scheduleId: Int)
    suspend fun deleteAllLaunchSchedules()
}