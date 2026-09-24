package com.galib.appscheduler.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galib.appscheduler.domain.model.AppInfo
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleResult
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.domain.usecase.LaunchScheduleUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class LaunchScheduleViewModel(
    private val launchScheduleUseCase: LaunchScheduleUseCase
) : ViewModel() {

    val launchSchedules: StateFlow<List<LaunchSchedule>> = launchScheduleUseCase.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Backwards compatibility alias for AppScheduleScreen
    val apps: StateFlow<List<LaunchSchedule>> = launchSchedules

    private val _scheduleEvent = MutableSharedFlow<ScheduleResult>()
    val scheduleEvent: SharedFlow<ScheduleResult> = _scheduleEvent.asSharedFlow()

    fun addSchedule(selectedApp: AppInfo, selectedDateTime: LocalDateTime) {
        viewModelScope.launch {
            val scheduledMillis = selectedDateTime.toInstant(
                ZoneOffset.systemDefault().rules.getOffset(selectedDateTime)
            ).toEpochMilli()

            val result = launchScheduleUseCase.schedule(
                LaunchSchedule(
                    packageName = selectedApp.packageName,
                    appName = selectedApp.appName,
                    scheduledTime = scheduledMillis,
                    status = ScheduleStatus.SCHEDULED
                )
            )
            _scheduleEvent.emit(result)
        }
    }

    fun updateLaunchSchedule(launchSchedule: LaunchSchedule, selectedDateTime: LocalDateTime) {
        viewModelScope.launch {
            val scheduledMillis = selectedDateTime.toInstant(
                ZoneOffset.systemDefault().rules.getOffset(selectedDateTime)
            ).toEpochMilli()
            val updatedLaunchSchedule = launchSchedule.copy(
                scheduledTime = scheduledMillis
            )
            val result = launchScheduleUseCase.update(updatedLaunchSchedule)
            _scheduleEvent.emit(result)
        }
    }

    fun cancelLaunchSchedule(launchSchedule: LaunchSchedule) {
        viewModelScope.launch {
            launchScheduleUseCase.cancel(launchSchedule)
        }
    }
}