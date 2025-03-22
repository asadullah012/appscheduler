package com.meldcx.appscheduler

import android.app.Application
import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.domain.usecase.LaunchScheduleUseCase
import com.meldcx.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class LaunchScheduleViewModelTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var viewModel: LaunchScheduleViewModel
    private val launchScheduleUseCase: LaunchScheduleUseCase = mockk(relaxed = true)
    private val application: Application = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = LaunchScheduleViewModel(launchScheduleUseCase, application)
    }

    @Test
    fun `loadLaunchSchedule should collect schedules from use case`() = runTest {
        // Arrange
        val schedules = listOf(
            LaunchSchedule(1, "com.example.app", "Example App", 1000L, SCHEDULE_STATUS.SCHEDULED)
        )
        every { launchScheduleUseCase.getAll() } returns flowOf(schedules)

        // Act
        viewModel.loadLaunchSchedule()

        // Assert
        Assertions.assertEquals(schedules, viewModel.apps.value)
    }

    @Test
    fun `addSchedule should call schedule in use case`() = runTest {
        // Arrange
        val selectedApp = AppInfo("com.example.app", "Example App", "1")
        val selectedDateTime = LocalDateTime.now()
        coEvery { launchScheduleUseCase.schedule(any()) } just Runs

        // Act
        viewModel.addSchedule(selectedApp, selectedDateTime)

        // Assert
        coVerify { launchScheduleUseCase.schedule(any()) }
    }

    @Test
    fun `updateLaunchSchedule should call update in use case`() = runTest {
        val launchSchedule = LaunchSchedule(1, "com.example.app", "Example App", 1000L, SCHEDULE_STATUS.SCHEDULED)
        val newTime = LocalDateTime.now()
        coEvery { launchScheduleUseCase.update(any()) } just Runs

        viewModel.updateLaunchSchedule(launchSchedule, newTime)

        coVerify { launchScheduleUseCase.update(any()) }
    }

    @Test
    fun `cancelLaunchSchedule should call cancel in use case`() = runTest {
        val launchSchedule = LaunchSchedule(1, "com.example.app", "Example App", 1000L, SCHEDULE_STATUS.SCHEDULED)
        coEvery { launchScheduleUseCase.cancel(any()) } just Runs

        viewModel.cancelLaunchSchedule(launchSchedule)

        coVerify { launchScheduleUseCase.cancel(launchSchedule) }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}