package com.meldcx.appscheduler.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleEntity(
    @PrimaryKey val scheduleId: Long,
    val packageName: String,
    val scheduledTime: Long,
    val status: String
)