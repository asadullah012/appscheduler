package com.meldcx.appscheduler.domain.usecase

import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.domain.repository.AppRepository

class GetAppsUseCase(private val repository: AppRepository) {
    operator fun invoke() = repository.getInstalledApps()
}

class SaveAppsUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(apps: List<AppInfo>) {
        repository.saveApps(apps)
    }
}

class SyncInstalledAppsUseCase(private val repository: AppRepository) {
    suspend operator fun invoke() {
        repository.syncInstalledApp()
    }
}