package com.meldcx.appscheduler.domain.usecase

import com.meldcx.appscheduler.domain.repository.AppRepository

class AppsUseCase(private val repository: AppRepository) {
    fun getAllApps() = repository.getInstalledApps()
}