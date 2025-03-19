package com.meldcx.appscheduler.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.domain.usecase.GetAppsUseCase
import com.meldcx.appscheduler.domain.usecase.SyncInstalledAppsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val getAppsUseCase: GetAppsUseCase,
    private val syncInstalledAppsUseCase: SyncInstalledAppsUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps = _apps.asStateFlow()

    init {
        loadInstalledApps()
    }

    fun loadInstalledApps() {
        viewModelScope.launch {
            getAppsUseCase().collect { _apps.value = it }
        }
    }

    fun getAppInfoByPackageName(packageName: String): AppInfo? {
        return apps.value.find { it.packageName == packageName }
    }

    fun syncInstalledApps(){
        viewModelScope.launch {
            syncInstalledAppsUseCase()
        }
    }
}