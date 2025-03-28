package com.meldcx.appscheduler.di

import android.app.Application
import androidx.room.Room
import com.meldcx.appscheduler.data.local.AppDatabase
import com.meldcx.appscheduler.data.local.SystemAppDataSource
import com.meldcx.appscheduler.data.repository.AppRepositoryImpl
import com.meldcx.appscheduler.data.repository.ScheduleRepositoryImpl
import com.meldcx.appscheduler.domain.repository.AppRepository
import com.meldcx.appscheduler.domain.repository.ScheduleRepository
import com.meldcx.appscheduler.domain.usecase.AppsUseCase
import com.meldcx.appscheduler.domain.usecase.LaunchScheduleUseCase
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
    single { androidContext().packageManager }
    single { SystemAppDataSource(get()) }
    single<AppRepository> { AppRepositoryImpl(get(), get()) }
    factory { AppsUseCase(get()) }
    viewModel { AppViewModel(get(), get()) }

    single { get<AppDatabase>().launchScheduleDao() }
    single<ScheduleRepository> { ScheduleRepositoryImpl(get()) }
    factory { LaunchScheduleUseCase(get(), get()) }
    viewModel { LaunchScheduleViewModel(get(), get()) }
}