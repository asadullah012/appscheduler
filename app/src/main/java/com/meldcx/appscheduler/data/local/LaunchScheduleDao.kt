package com.meldcx.appscheduler.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.meldcx.appscheduler.data.model.LaunchScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchScheduleDao {
    @Query("SELECT * FROM launch_schedule")
    fun getAllLaunchSchedules(): Flow<List<LaunchScheduleEntity>>

    @Query("SELECT * FROM launch_schedule WHERE scheduleId = :scheduleId")
    fun getLaunchSchedulesByScheduleId(scheduleId: Int): Flow<LaunchScheduleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunchSchedule(launchSchedule: LaunchScheduleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLaunchSchedule(launchSchedule: LaunchScheduleEntity)

    @Query("DELETE FROM launch_schedule")
    suspend fun deleteAllaLaunchSchedules()

    @Query("DELETE FROM launch_schedule WHERE scheduleId = :scheduleId")
    suspend fun deleteLaunchSchedulesByScheduleId(scheduleId: Int)
}