package com.galib.appscheduler.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.galib.appscheduler.domain.model.SCHEDULE_STATUS

@Entity(tableName = "launch_schedule")
data class LaunchScheduleEntity(
    @PrimaryKey(autoGenerate = true) val scheduleId: Int = 0,
    val packageName: String,
    val appName: String,
    val scheduledTime: Long,
    val status: Int
)