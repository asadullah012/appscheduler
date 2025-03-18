package com.galib.appscheduler.di

import android.app.Application
import androidx.room.Room
import com.galib.appscheduler.data.local.AppDatabase
import com.galib.appscheduler.data.local.SystemAppDataSource
import com.galib.appscheduler.data.repository.AppRepositoryImpl
import com.galib.appscheduler.domain.repository.AppRepository
import com.galib.appscheduler.domain.usecase.GetAppsUseCase
import com.galib.appscheduler.domain.usecase.SaveAppsUseCase
import com.galib.appscheduler.domain.usecase.SyncInstalledAppsUseCase
import com.galib.appscheduler.presentation.viewmodel.AppViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(get<Application>(), AppDatabase::class.java, "app_db").build()
    }
    single { get<AppDatabase>().appDao() }
    single { androidContext().packageManager }
    single { SystemAppDataSource(get()) }
    single<AppRepository> { AppRepositoryImpl(get(), get()) }
    factory { GetAppsUseCase(get()) }
    factory { SaveAppsUseCase(get()) }
    factory { SyncInstalledAppsUseCase(get()) }
    viewModel { AppViewModel(get(), get(), get()) }
}