package com.galib.appscheduler.domain.usecase

import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleResult
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.domain.repository.ScheduleRepository
import com.galib.appscheduler.domain.scheduler.AlarmScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LaunchScheduleUseCaseTest {

    private lateinit var repository: ScheduleRepository
    private lateinit var alarmScheduler: AlarmScheduler
    private lateinit var useCase: LaunchScheduleUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        alarmScheduler = mockk(relaxed = true)
        useCase = LaunchScheduleUseCase(alarmScheduler, repository)
    }

    @Test
    fun `schedule returns PastTimeError when target time is in the past`() = runTest {
        val pastSchedule = LaunchSchedule(
            packageName = "com.test.app",
            appName = "Test App",
            scheduledTime = System.currentTimeMillis() - 50_000L,
            status = ScheduleStatus.SCHEDULED
        )

        val result = useCase.schedule(pastSchedule)

        assertEquals(ScheduleResult.PastTimeError, result)
        coVerify(exactly = 0) { repository.scheduleAppLaunch(any()) }
        verify(exactly = 0) { alarmScheduler.schedule(any()) }
    }

    @Test
    fun `schedule returns Conflict when an existing schedule conflicts`() = runTest {
        val futureTime = System.currentTimeMillis() + 600_000L
        val newSchedule = LaunchSchedule(
            packageName = "com.test.newapp",
            appName = "New App",
            scheduledTime = futureTime,
            status = ScheduleStatus.SCHEDULED
        )
        val existingConflicting = LaunchSchedule(
            scheduleId = 1,
            packageName = "com.test.existing",
            appName = "Existing App",
            scheduledTime = futureTime + 20_000L,
            status = ScheduleStatus.SCHEDULED
        )
        coEvery { repository.findConflictingSchedule(futureTime) } returns existingConflicting

        val result = useCase.schedule(newSchedule)

        assertTrue(result is ScheduleResult.Conflict)
        assertEquals("Existing App", (result as ScheduleResult.Conflict).conflictingSchedule.appName)
        coVerify(exactly = 0) { repository.scheduleAppLaunch(any()) }
    }

    @Test
    fun `schedule inserts in repository and schedules alarm when valid`() = runTest {
        val futureTime = System.currentTimeMillis() + 600_000L
        val validSchedule = LaunchSchedule(
            packageName = "com.test.app",
            appName = "Test App",
            scheduledTime = futureTime,
            status = ScheduleStatus.SCHEDULED
        )
        coEvery { repository.findConflictingSchedule(futureTime) } returns null
        coEvery { repository.scheduleAppLaunch(any()) } returns 7
        every { alarmScheduler.schedule(any()) } returns true

        val result = useCase.schedule(validSchedule)

        assertTrue(result is ScheduleResult.Success)
        assertEquals(7, (result as ScheduleResult.Success).scheduleId)
        verify(exactly = 1) { alarmScheduler.schedule(match { it.scheduleId == 7 }) }
    }

    @Test
    fun `cancel delegates to repository and cancels alarm`() = runTest {
        val schedule = LaunchSchedule(
            scheduleId = 3,
            packageName = "com.test.app",
            appName = "Test App",
            scheduledTime = 1000L,
            status = ScheduleStatus.SCHEDULED
        )

        useCase.cancel(schedule)

        coVerify(exactly = 1) { repository.cancelLaunchSchedule(schedule) }
        verify(exactly = 1) { alarmScheduler.cancel(schedule) }
    }

    @Test
    fun `getAll returns all launch schedules from repository`() = runTest {
        val expected = listOf(
            LaunchSchedule(1, "com.app.a", "App A", 1000L, ScheduleStatus.SCHEDULED),
            LaunchSchedule(2, "com.app.b", "App B", 2000L, ScheduleStatus.EXECUTED)
        )
        every { repository.getAllLaunchSchedules() } returns flowOf(expected)

        val result = useCase.getAll().first()

        assertEquals(expected, result)
    }

    @Test
    fun `getByScheduleId returns specific schedule from repository`() = runTest {
        val expected = LaunchSchedule(1, "com.app.a", "App A", 1000L, ScheduleStatus.SCHEDULED)
        every { repository.getLaunchSchedulesByScheduleId(1) } returns flowOf(expected)

        val result = useCase.getByScheduleId(1).first()

        assertEquals(expected, result)
    }

    @Test
    fun `deleteByScheduleId delegates to repository`() = runTest {
        useCase.deleteByScheduleId(1)

        coVerify(exactly = 1) { repository.deleteLaunchSchedulesByScheduleId(1) }
    }
}
