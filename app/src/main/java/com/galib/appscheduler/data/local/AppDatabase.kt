package com.galib.appscheduler.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galib.appscheduler.data.model.AppEntity
import com.galib.appscheduler.data.model.LaunchScheduleEntity

@Database(entities = [AppEntity::class, LaunchScheduleEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun launchScheduleDao(): LaunchScheduleDao
}