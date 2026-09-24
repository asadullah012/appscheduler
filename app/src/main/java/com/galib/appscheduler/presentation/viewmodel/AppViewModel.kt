package com.galib.appscheduler.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galib.appscheduler.domain.model.AppInfo
import com.galib.appscheduler.domain.usecase.AppsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface AppListUiState {
    data object Loading : AppListUiState
    data class Success(val apps: List<AppInfo>) : AppListUiState
    data class Error(val message: String) : AppListUiState
}

class AppViewModel(
    private val appsUseCase: AppsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppListUiState>(AppListUiState.Loading)
    val uiState: StateFlow<AppListUiState> = _uiState.asStateFlow()

    // Backwards compatibility list
    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    init {
        loadInstalledApps()
    }

    fun loadInstalledApps() {
        viewModelScope.launch {
            _uiState.value = AppListUiState.Loading
            appsUseCase.getAllApps()
                .catch { e ->
                    _uiState.value = AppListUiState.Error(e.message ?: "Failed to load apps")
                }
                .collect { apps ->
                    _apps.value = apps
                    _uiState.value = AppListUiState.Success(apps)
                }
        }
    }
}