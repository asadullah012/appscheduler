package com.galib.appscheduler.domain.usecase

import com.galib.appscheduler.domain.repository.AppRepository

class AppsUseCase(private val repository: AppRepository) {
    fun getAllApps() = repository.getInstalledApps()
}