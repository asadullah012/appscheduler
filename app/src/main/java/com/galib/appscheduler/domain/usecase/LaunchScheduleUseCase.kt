package com.galib.appscheduler.domain.usecase

import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.repository.AppRepository


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