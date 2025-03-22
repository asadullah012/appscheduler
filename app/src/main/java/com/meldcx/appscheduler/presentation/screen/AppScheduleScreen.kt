package com.meldcx.appscheduler.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
import com.meldcx.appscheduler.utils.formatTimestamp
import com.meldcx.appscheduler.utils.getIconDrawableByPackageName
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
                    ScheduleItem(launchScheduleViewModel, launchSchedule)
                }
            }
        }
    }
}

@Composable
fun ScheduleItem(launchScheduleViewModel: LaunchScheduleViewModel, launchSchedule: LaunchSchedule) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        onClick = {
            if(launchSchedule.status == SCHEDULE_STATUS.SCHEDULED) {
                showBottomSheet = true
            } else {
                Toast.makeText(context, "Unable to reschedule app!", Toast.LENGTH_SHORT).show()
            }
        }
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically){
            Image(bitmap = getIconDrawableByPackageName(context, launchSchedule.packageName), contentDescription = launchSchedule.appName,
                modifier = Modifier.size(72.dp))
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "App Name: ${launchSchedule.appName}", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = formatTimestamp(launchSchedule.scheduledTime), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Text(text = launchSchedule.status.toString(), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        }
    }
    if(showBottomSheet){
        ScheduleBottomSheet(
            title = "Reschedule App: ${launchSchedule.appName}",
            reschedule = true,
            scheduledTime = launchSchedule.scheduledTime,
            showBottomSheet = showBottomSheet,
            onSchedule = { selectedDateTime ->
                if(selectedDateTime != null){
                    launchScheduleViewModel.updateLaunchSchedule(launchSchedule, selectedDateTime)
                }
            },
            onCancelSchedule = {
                launchScheduleViewModel.cancelLaunchSchedule(launchSchedule)
            },
            onDismissRequest = {
                showBottomSheet = false
            }
        )
    }
}
