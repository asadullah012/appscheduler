package com.galib.appscheduler.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.usecase.GetLaunchScheduleUseCase
import com.galib.appscheduler.domain.usecase.InsertLaunchScheduleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LaunchScheduleViewModel(
    private val getLaunchScheduleUseCase: GetLaunchScheduleUseCase,
    private val insertLaunchScheduleUseCase: InsertLaunchScheduleUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _launchSchedule = MutableStateFlow<List<LaunchSchedule>>(emptyList())
    val apps = _launchSchedule.asStateFlow()

    init {
        loadLaunchSchedule()
    }

    fun loadLaunchSchedule() {
        viewModelScope.launch {
            getLaunchScheduleUseCase().collect { _launchSchedule.value = it }
        }
    }

    fun insertLaunchSchedule(launchSchedule: LaunchSchedule){
        viewModelScope.launch {
            insertLaunchScheduleUseCase(launchSchedule)
        }
    }
}