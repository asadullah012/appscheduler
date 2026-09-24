package com.galib.appscheduler.data.repository

import com.galib.appscheduler.data.local.LaunchScheduleDao
import com.galib.appscheduler.data.model.LaunchScheduleEntity
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScheduleRepositoryImplTest {

    private lateinit var dao: LaunchScheduleDao
    private lateinit var repository: ScheduleRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = ScheduleRepositoryImpl(dao)
    }

    @Test
    fun `scheduleAppLaunch inserts entity and returns generated id`() = runTest {
        val schedule = LaunchSchedule(
            scheduleId = 0,
            packageName = "com.example.app",
            appName = "Example App",
            scheduledTime = 1700000000000L,
            status = ScheduleStatus.SCHEDULED
        )
        coEvery { dao.insertLaunchSchedule(any()) } returns 42L

        val result = repository.scheduleAppLaunch(schedule)

        assertEquals(42, result)
        coVerify(exactly = 1) {
            dao.insertLaunchSchedule(match {
                it.packageName == "com.example.app" &&
                it.appName == "Example App" &&
                it.scheduledTime == 1700000000000L &&
                it.status == ScheduleStatus.SCHEDULED.ordinal
            })
        }
    }

    @Test
    fun `cancelLaunchSchedule updates entity status to CANCELLED_BY_USER`() = runTest {
        val schedule = LaunchSchedule(
            scheduleId = 5,
            packageName = "com.example.app",
            appName = "Example App",
            scheduledTime = 1700000000000L,
            status = ScheduleStatus.SCHEDULED
        )

        repository.cancelLaunchSchedule(schedule)

        coVerify(exactly = 1) {
            dao.updateLaunchSchedule(match {
                it.scheduleId == 5 &&
                it.status == ScheduleStatus.CANCELLED_BY_USER.ordinal
            })
        }
    }

    @Test
    fun `getAllLaunchSchedules maps entities to domain models correctly`() = runTest {
        val entityList = listOf(
            LaunchScheduleEntity(
                scheduleId = 1,
                packageName = "com.app.one",
                appName = "App One",
                scheduledTime = 1000L,
                status = ScheduleStatus.SCHEDULED.ordinal
            ),
            LaunchScheduleEntity(
                scheduleId = 2,
                packageName = "com.app.two",
                appName = "App Two",
                scheduledTime = 2000L,
                status = ScheduleStatus.EXECUTED.ordinal
            )
        )
        every { dao.getAllLaunchSchedules() } returns flowOf(entityList)

        val result = repository.getAllLaunchSchedules().first()

        assertEquals(2, result.size)
        assertEquals("App One", result[0].appName)
        assertEquals(ScheduleStatus.SCHEDULED, result[0].status)
        assertEquals("App Two", result[1].appName)
        assertEquals(ScheduleStatus.EXECUTED, result[1].status)
    }

    @Test
    fun `deleteLaunchSchedulesByScheduleId delegates to DAO`() = runTest {
        repository.deleteLaunchSchedulesByScheduleId(10)

        coVerify(exactly = 1) { dao.deleteLaunchSchedulesByScheduleId(10) }
    }

    @Test
    fun `deleteAllLaunchSchedules delegates to DAO`() = runTest {
        repository.deleteAllLaunchSchedules()

        coVerify(exactly = 1) { dao.deleteAllLaunchSchedules() }
    }

    @Test
    fun `findConflictingSchedule delegates to DAO with buffer range`() = runTest {
        val targetTime = 500_000L
        val buffer = 60_000L
        val entity = LaunchScheduleEntity(
            scheduleId = 9,
            packageName = "com.conflict.app",
            appName = "Conflict App",
            scheduledTime = 510_000L,
            status = ScheduleStatus.SCHEDULED.ordinal
        )
        coEvery { dao.findConflictingSchedule(440_000L, 560_000L, -1) } returns entity

        val result = repository.findConflictingSchedule(targetTime, buffer, -1)

        assertEquals("Conflict App", result?.appName)
        assertEquals(9, result?.scheduleId)
    }
}
