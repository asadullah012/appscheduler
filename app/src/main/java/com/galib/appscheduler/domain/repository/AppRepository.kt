package com.galib.appscheduler.domain.repository

import com.galib.appscheduler.domain.model.AppInfo
import com.galib.appscheduler.domain.model.LaunchSchedule
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getInstalledApps(): Flow<List<AppInfo>>
}