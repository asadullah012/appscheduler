package com.galib.appscheduler.domain.usecase

import android.content.Context
import com.galib.appscheduler.domain.model.LaunchSchedule
import com.galib.appscheduler.domain.model.ScheduleStatus
import com.galib.appscheduler.domain.repository.ScheduleRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LaunchScheduleUseCaseTest {

    private lateinit var repository: ScheduleRepository
    private lateinit var context: Context
    private lateinit var useCase: LaunchScheduleUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        context = mockk(relaxed = true)
        useCase = LaunchScheduleUseCase(context, repository)
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
    fun `getScheduledAppsByStatus filters by status`() = runTest {
        val expected = listOf(
            LaunchSchedule(1, "com.app.a", "App A", 1000L, ScheduleStatus.SCHEDULED)
        )
        every { repository.getScheduleByStatus(ScheduleStatus.SCHEDULED) } returns flowOf(expected)

        val result = useCase.getScheduledAppsByStatus(ScheduleStatus.SCHEDULED).first()

        assertEquals(expected, result)
    }

    @Test
    fun `deleteByScheduleId delegates to repository`() = runTest {
        useCase.deleteByScheduleId(1)

        coVerify(exactly = 1) { repository.deleteLaunchSchedulesByScheduleId(1) }
    }

    @Test
    fun `deleteAllSchedule delegates to repository`() = runTest {
        useCase.deleteAllSchedule()

        coVerify(exactly = 1) { repository.deleteAllLaunchSchedules() }
    }
}
