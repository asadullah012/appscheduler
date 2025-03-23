package com.meldcx.appscheduler.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.meldcx.appscheduler.data.model.LaunchScheduleEntity
import com.meldcx.appscheduler.domain.model.ScheduleStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchScheduleDao {
    @Query("SELECT * FROM launch_schedule ORDER BY status, scheduledTime ASC")
    fun getAllLaunchSchedules(): Flow<List<LaunchScheduleEntity>>

    @Query("SELECT * FROM launch_schedule WHERE scheduleId = :scheduleId")
    fun getLaunchSchedulesByScheduleId(scheduleId: Int): Flow<LaunchScheduleEntity>

    @Query("SELECT * FROM launch_schedule WHERE status = :status")
    fun getScheduleByStatus(status: Int): Flow<List<LaunchScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunchSchedule(launchSchedule: LaunchScheduleEntity): Long

    @Update
    suspend fun updateLaunchSchedule(launchSchedule: LaunchScheduleEntity)

    @Query("DELETE FROM launch_schedule")
    suspend fun deleteAllaLaunchSchedules()

    @Query("DELETE FROM launch_schedule WHERE scheduleId = :scheduleId")
    suspend fun deleteLaunchSchedulesByScheduleId(scheduleId: Int)
}