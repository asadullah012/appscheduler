package com.meldcx.appscheduler.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.usecase.LaunchScheduleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LaunchScheduleViewModel(
    private val launchScheduleUseCase: LaunchScheduleUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _launchSchedule = MutableStateFlow<List<LaunchSchedule>>(emptyList())
    val apps = _launchSchedule.asStateFlow()

    init {
        loadLaunchSchedule()
    }

    fun loadLaunchSchedule() {
        viewModelScope.launch {
            launchScheduleUseCase.getAll().collect { _launchSchedule.value = it }
        }
    }

    fun insertLaunchSchedule(launchSchedule: LaunchSchedule){
        viewModelScope.launch {
            launchScheduleUseCase.schedule(launchSchedule)
        }
    }
}