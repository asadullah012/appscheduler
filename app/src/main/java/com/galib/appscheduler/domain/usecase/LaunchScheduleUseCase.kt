package com.galib.appscheduler.domain.usecase

import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.repository.ScheduleRepository


class GetLaunchScheduleUseCase(private val repository: ScheduleRepository) {
    operator fun invoke() = repository.getLaunchSchedules()
}

class InsertLaunchScheduleUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke(launchSchedule: LaunchSchedule) {
        repository.insertLaunchSchedule(launchSchedule)
    }
}

class DeleteAllLaunchScheduleUseCase(private val repository: ScheduleRepository) {
    suspend operator fun invoke() {
        repository.deleteAllLaunchSchedule()
    }
}

class ScheduleAppLaunchUseCase() {
    operator fun invoke(launchSchedule: LaunchSchedule) {

    }
}
