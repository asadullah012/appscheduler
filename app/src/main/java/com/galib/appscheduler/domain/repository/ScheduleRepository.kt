package com.galib.appscheduler.domain.repository

import com.galib.appscheduler.domain.model.LaunchSchedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getLaunchSchedules(): Flow<List<LaunchSchedule>>
    suspend fun insertLaunchSchedule(launchSchedule: LaunchSchedule)
    suspend fun deleteAllLaunchSchedule()
}