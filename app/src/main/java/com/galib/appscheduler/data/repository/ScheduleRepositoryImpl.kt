package com.galib.appscheduler.data.repository

import android.app.PendingIntent
import com.galib.appscheduler.data.local.LaunchScheduleDao
import com.galib.appscheduler.data.mapper.toDomain
import com.galib.appscheduler.data.mapper.toEntity
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl (
    private val launchScheduleDao: LaunchScheduleDao
) : ScheduleRepository {
    private val pendingIntents = mutableMapOf<Long, PendingIntent>()

    override fun getLaunchSchedules(): Flow<List<LaunchSchedule>> {
        val launchSchedules = launchScheduleDao.getLaunchSchedules()
        return launchSchedules.map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun insertLaunchSchedule(launchSchedule: LaunchSchedule) {
        launchScheduleDao.insertLaunchSchedule(launchSchedule.toEntity())
    }

    override suspend fun deleteAllLaunchSchedule() {
        launchScheduleDao.deleteAllaLaunchSchedules()
    }
}