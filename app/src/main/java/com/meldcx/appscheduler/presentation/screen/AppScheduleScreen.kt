package com.meldcx.appscheduler.presentation.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.presentation.viewmodel.AppViewModel
import com.meldcx.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
import com.meldcx.appscheduler.utils.formatTimestamp
import com.meldcx.appscheduler.utils.getIconDrawableByPackageName
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppScheduleScreen() {
    val launchScheduleViewModel: LaunchScheduleViewModel = koinViewModel()
    val launchSchedules by launchScheduleViewModel.apps.collectAsState()
    val appViewModel: AppViewModel = koinViewModel()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if(launchSchedules.isEmpty()){
            Text("No Scheduled Apps")
        } else {
            LazyColumn {
                items(launchSchedules) { launchSchedule ->
                    ScheduleItem(launchSchedule, appViewModel.getAppInfoByPackageName(launchSchedule.packageName))
                }
            }
        }
    }
}

@Composable
fun ScheduleItem(launchSchedule: LaunchSchedule, appInfo: AppInfo?) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically){
            Image(bitmap = getIconDrawableByPackageName(context, launchSchedule.packageName), contentDescription = appInfo?.appName,
                modifier = Modifier.size(72.dp))
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                appInfo?.let {
                    Text(text = "App Name: ${it.appName}", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(text = formatTimestamp(launchSchedule.scheduledTime), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
                Text(text = launchSchedule.status.toString(), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        }

    }
}
