package com.galib.appscheduler.di

import android.app.Application
import androidx.room.Room
import com.galib.appscheduler.data.local.AppDatabase
import com.galib.appscheduler.data.local.SystemAppDataSource
import com.galib.appscheduler.data.repository.AppRepositoryImpl
import com.galib.appscheduler.data.repository.ScheduleRepositoryImpl
import com.galib.appscheduler.domain.repository.AppRepository
import com.galib.appscheduler.domain.repository.ScheduleRepository
import com.galib.appscheduler.domain.scheduler.AlarmScheduler
import com.galib.appscheduler.domain.usecase.AppsUseCase
import com.galib.appscheduler.domain.usecase.LaunchScheduleUseCase
import com.galib.appscheduler.frameworks.AndroidAlarmScheduler
import com.galib.appscheduler.presentation.viewmodel.AppViewModel
import com.galib.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
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
    single { androidContext().packageManager }
    single { SystemAppDataSource(get()) }
    single<AppRepository> { AppRepositoryImpl(get(), get()) }
    factory { AppsUseCase(get()) }
    viewModel { AppViewModel(get()) }

    single { get<AppDatabase>().launchScheduleDao() }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get()) }
    single<AlarmScheduler> { AndroidAlarmScheduler(androidContext()) }
    factory { LaunchScheduleUseCase(get(), get()) }
    viewModel { LaunchScheduleViewModel(get()) }
}