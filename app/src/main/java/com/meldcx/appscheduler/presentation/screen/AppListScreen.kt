package com.meldcx.appscheduler.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.presentation.viewmodel.AppViewModel
import com.meldcx.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
import com.meldcx.appscheduler.utils.getIconDrawableByPackageName
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppListScreen() {
    val appViewModel: AppViewModel = koinViewModel()
    val apps by appViewModel.apps.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 100.dp)
        ) {
            items(apps) { app ->
                InstalledAppItem(app)
            }
        }
    }

}

@Composable
fun InstalledAppItem(app: AppInfo){
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    val launchScheduleViewModel: LaunchScheduleViewModel = koinViewModel()

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        onClick = {
            showBottomSheet = true
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Image(bitmap = getIconDrawableByPackageName(context, app.packageName), contentDescription = app.appName,
                modifier = Modifier.size(72.dp))
            Text(text = app.appName, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
    }
    if(showBottomSheet){
        ScheduleBottomSheet(
            title = "Schedule App: ${app.appName}",
            showBottomSheet = showBottomSheet,
            onSchedule = { selectedDateTime ->
                if(selectedDateTime != null )
                    launchScheduleViewModel.addSchedule(app, selectedDateTime)
            },
            onDismissRequest = { showBottomSheet = false }
        )
    }
}
