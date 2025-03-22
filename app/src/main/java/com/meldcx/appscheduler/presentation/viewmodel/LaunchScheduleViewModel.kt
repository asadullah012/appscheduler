package com.meldcx.appscheduler.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.domain.usecase.LaunchScheduleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class LaunchScheduleViewModel(
    private val launchScheduleUseCase: LaunchScheduleUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _launchSchedule = MutableStateFlow<List<LaunchSchedule>>(emptyList())
    val apps = _launchSchedule.asStateFlow()

    init {
        loadLaunchSchedule()
    }

    private fun loadLaunchSchedule() {
        viewModelScope.launch {
            launchScheduleUseCase.getAll().collect { _launchSchedule.value = it }
        }
    }

    fun addSchedule(selectedApp: AppInfo, selectedDate: LocalDate, selectedTime: LocalTime){

        viewModelScope.launch {
            val scheduledDateTime = LocalDateTime.of(selectedDate, selectedTime)
            //val scheduledDateTime = LocalDateTime.now()
            val scheduledMillis = scheduledDateTime.toInstant(ZoneOffset.systemDefault().rules.getOffset(scheduledDateTime)).toEpochMilli()

            launchScheduleUseCase.schedule(LaunchSchedule(
                packageName = selectedApp.packageName,
                appName = selectedApp.appName,
                scheduledTime = scheduledMillis,
                status = SCHEDULE_STATUS.SCHEDULED
            ))
        }
    }

    fun updateLaunchSchedule(launchSchedule: LaunchSchedule, selectedDate: LocalDate, selectedTime: LocalTime){
        viewModelScope.launch {
            val scheduledDateTime = LocalDateTime.of(selectedDate, selectedTime)
//          val scheduledDateTime = LocalDateTime.now()
            val scheduledMillis = scheduledDateTime.toInstant(ZoneOffset.systemDefault().rules.getOffset(scheduledDateTime)).toEpochMilli()
            val updatedLaunchSchedule = launchSchedule.copy(
                scheduledTime = scheduledMillis
            )
            launchScheduleUseCase.update(updatedLaunchSchedule)
        }
    }

    fun cancelLaunchSchedule(launchSchedule: LaunchSchedule){
        viewModelScope.launch {
            launchScheduleUseCase.cancel(launchSchedule)
        }
    }
}