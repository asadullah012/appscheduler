package com.meldcx.appscheduler.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppScheduleScreen() {
    val launchScheduleViewModel: LaunchScheduleViewModel = koinViewModel()
    val launchSchedules by launchScheduleViewModel.apps.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if(launchSchedules.isEmpty()){
            Text("No Scheduled Apps")
        } else {
            LazyColumn {
                items(launchSchedules) { launchSchedule ->
                    ScheduleItem(launchSchedule)
                }
            }
        }
    }
}

@Composable
fun ScheduleItem(launchSchedule: LaunchSchedule) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(text = launchSchedule.packageName, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            Text(text = launchSchedule.scheduledTime.toString(), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            Text(text = launchSchedule.status.toString(), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
    }
}
