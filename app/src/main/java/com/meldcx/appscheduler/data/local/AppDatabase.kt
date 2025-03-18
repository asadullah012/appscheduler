package com.meldcx.appscheduler.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meldcx.appscheduler.data.model.AppEntity

@Database(entities = [AppEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}