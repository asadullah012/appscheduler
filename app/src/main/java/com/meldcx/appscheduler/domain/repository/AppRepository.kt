package com.meldcx.appscheduler.domain.repository

import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getInstalledApps(): Flow<List<AppInfo>>
    suspend fun saveApps(apps: List<AppInfo>)
    suspend fun syncInstalledApp()

    fun getLaunchSchedules(): Flow<List<LaunchSchedule>>
    suspend fun insertLaunchSchedule(launchSchedule: LaunchSchedule)
    suspend fun deleteAllLaunchSchedule()
}