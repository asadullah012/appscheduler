package com.meldcx.appscheduler.di

import android.app.Application
import androidx.room.Room
import com.meldcx.appscheduler.data.local.AppDatabase
import com.meldcx.appscheduler.data.local.SystemAppDataSource
import com.meldcx.appscheduler.data.repository.AppRepositoryImpl
import com.meldcx.appscheduler.domain.repository.AppRepository
import com.meldcx.appscheduler.domain.usecase.DeleteAllLaunchScheduleUseCase
import com.meldcx.appscheduler.domain.usecase.GetAppsUseCase
import com.meldcx.appscheduler.domain.usecase.GetLaunchScheduleUseCase
import com.meldcx.appscheduler.domain.usecase.InsertLaunchScheduleUseCase
import com.meldcx.appscheduler.domain.usecase.SaveAppsUseCase
import com.meldcx.appscheduler.domain.usecase.SyncInstalledAppsUseCase
import com.meldcx.appscheduler.presentation.viewmodel.AppViewModel
import com.meldcx.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(get<Application>(), AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().appDao() }
    single { get<AppDatabase>().launchScheduleDao() }
    single { androidContext().packageManager }
    single { SystemAppDataSource(get()) }
    single<AppRepository> { AppRepositoryImpl(get(), get(), get()) }
    factory { GetAppsUseCase(get()) }
    factory { SaveAppsUseCase(get()) }
    factory { SyncInstalledAppsUseCase(get()) }
    viewModel { AppViewModel(get(), get(), get()) }

    factory { GetLaunchScheduleUseCase(get()) }
    factory { InsertLaunchScheduleUseCase(get()) }
    factory { DeleteAllLaunchScheduleUseCase(get()) }
    viewModel { LaunchScheduleViewModel(get(), get(), get()) }
}