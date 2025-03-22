package com.galib.appscheduler.data.repository

import com.galib.appscheduler.data.local.AppDao
import com.galib.appscheduler.data.local.SystemAppDataSource
import com.galib.appscheduler.data.mapper.toDomain
import com.galib.appscheduler.domain.model.AppInfo
import com.galib.appscheduler.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class AppRepositoryImpl(
    private val systemAppDataSource: SystemAppDataSource,
    private val dao: AppDao
) : AppRepository {
    override fun getInstalledApps(): Flow<List<AppInfo>> {
        return dao.getApps().onStart {
                val installedApps = systemAppDataSource.fetchLauncherInstalledApps()
                dao.deleteAllApps()
                dao.insertApps(installedApps)
            }.map { entities -> entities.map { it.toDomain() } }
    }
}