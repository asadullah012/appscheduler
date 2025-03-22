package com.meldcx.appscheduler

import android.content.Context
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.domain.repository.ScheduleRepository
import com.meldcx.appscheduler.domain.usecase.LaunchScheduleUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LaunchScheduleUseCaseTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var useCase: LaunchScheduleUseCase
    private val context: Context = mockk(relaxed = true)
    private val repository: ScheduleRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        useCase = LaunchScheduleUseCase(context, repository)
    }

    @Test
    fun `schedule should call repository and set alarm`() = runTest {
        // Arrange
        val launchSchedule = LaunchSchedule(0, "com.example.app", "Example App", 1000L, SCHEDULE_STATUS.SCHEDULED)
        coEvery { repository.scheduleAppLaunch(launchSchedule) } returns 1

        // Act
        useCase.schedule(launchSchedule)

        // Assert
        coVerify { repository.scheduleAppLaunch(launchSchedule) }
    }

    @Test
    fun `update should call repository update`() = runTest {
        val launchSchedule = LaunchSchedule(1, "com.example.app", "Example App", 1000L, SCHEDULE_STATUS.SCHEDULED)
        coEvery { repository.updateLaunchSchedule(launchSchedule) } just Runs

        useCase.update(launchSchedule)

        coVerify { repository.updateLaunchSchedule(launchSchedule) }
    }

    @Test
    fun `cancel should call repository cancel and cancel alarm`() = runTest {
        val launchSchedule = LaunchSchedule(1, "com.example.app", "Example App", 1000L, SCHEDULE_STATUS.SCHEDULED)
        coEvery { repository.cancelLaunchSchedule(launchSchedule) } just Runs

        useCase.cancel(launchSchedule)

        coVerify { repository.cancelLaunchSchedule(launchSchedule) }
    }

    @Test
    fun `getAll should return data from repository`() = runTest {
        val schedules = listOf(LaunchSchedule(1, "com.example.app", "Example App", 1000L, SCHEDULE_STATUS.SCHEDULED))
        every { repository.getAllLaunchSchedules() } returns flowOf(schedules)

        val result = useCase.getAll().first()

        assertEquals(schedules, result)
    }
}
