package com.meldcx.appscheduler.domain.usecase

import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.repository.AppRepository


class GetLaunchScheduleUseCase(private val repository: AppRepository) {
    operator fun invoke() = repository.getLaunchSchedules()
}

class InsertLaunchScheduleUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(launchSchedule: LaunchSchedule) {
        repository.insertLaunchSchedule(launchSchedule)
    }
}

class DeleteAllLaunchScheduleUseCase(private val repository: AppRepository) {
    suspend operator fun invoke() {
        repository.deleteAllLaunchSchedule()
    }
}