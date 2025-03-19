package com.meldcx.appscheduler.data.repository

import android.app.PendingIntent
import com.meldcx.appscheduler.data.local.LaunchScheduleDao
import com.meldcx.appscheduler.data.mapper.toDomain
import com.meldcx.appscheduler.data.mapper.toEntity
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.repository.ScheduleRepository
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