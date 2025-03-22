package com.meldcx.appscheduler.data.repository

import com.meldcx.appscheduler.data.local.LaunchScheduleDao
import com.meldcx.appscheduler.data.mapper.toDomain
import com.meldcx.appscheduler.data.mapper.toEntity
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl(
    private val launchScheduleDao: LaunchScheduleDao
) : ScheduleRepository {
    override suspend fun scheduleAppLaunch(launchSchedule: LaunchSchedule) : Int {
        return launchScheduleDao.insertLaunchSchedule(launchSchedule.toEntity()).toInt()
    }

    override suspend fun updateLaunchSchedule(launchSchedule: LaunchSchedule) {
        launchScheduleDao.updateLaunchSchedule(launchSchedule.toEntity())
    }

    override suspend fun cancelLaunchSchedule(launchSchedule: LaunchSchedule) {
        val cancelledLaunchSchedule = launchSchedule.copy(status = SCHEDULE_STATUS.CANCELLED_BY_USER)
        launchScheduleDao.updateLaunchSchedule(cancelledLaunchSchedule.toEntity())
    }

    override fun getLaunchSchedulesByScheduleId(scheduleId: Int): Flow<LaunchSchedule> {
        return launchScheduleDao.getLaunchSchedulesByScheduleId(scheduleId).map {
            it.toDomain()
        }
    }

    override fun getAllLaunchSchedules(): Flow<List<LaunchSchedule>> {
        return launchScheduleDao.getAllLaunchSchedules().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getScheduleByStatus(status: SCHEDULE_STATUS): Flow<List<LaunchSchedule>> {
        return launchScheduleDao.getScheduleByStatus(status).map { entities ->
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