package com.meldcx.appscheduler.data.repository

import android.content.Context
import android.util.Log
import com.meldcx.appscheduler.data.local.LaunchScheduleDao
import com.meldcx.appscheduler.data.mapper.toDomain
import com.meldcx.appscheduler.data.mapper.toEntity
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl (
    private val context: Context,
    private val launchScheduleDao: LaunchScheduleDao
) : ScheduleRepository {
    override suspend fun scheduleAppLaunch(launchSchedule: LaunchSchedule) {

        launchScheduleDao.insertLaunchSchedule(launchSchedule.toEntity())
    }

    override suspend fun updateLaunchSchedule(launchSchedule: LaunchSchedule) {

        launchScheduleDao.updateLaunchSchedule(launchSchedule.toEntity())
    }

    override suspend fun cancelLaunchSchedule(launchSchedule: LaunchSchedule) {

        launchScheduleDao.updateLaunchSchedule(launchSchedule.toEntity())
    }

    override fun getLaunchSchedulesByScheduleId(scheduleId: Int): Flow<LaunchSchedule> {

        return launchScheduleDao.getLaunchSchedulesByScheduleId(scheduleId).map {
            Log.d("TAG", "getLaunchSchedulesByScheduleId: ${it.packageName}")
            it.toDomain()
        }
    }

    override fun getAllLaunchSchedules(): Flow<List<LaunchSchedule>> {

        return launchScheduleDao.getAllLaunchSchedules().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteLaunchSchedulesByScheduleId(scheduleId: Int) {

        launchScheduleDao.deleteLaunchSchedulesByScheduleId(scheduleId)
    }

    override suspend fun deleteAllLaunchSchedules() {

        launchScheduleDao.deleteAllaLaunchSchedules()
    }


}