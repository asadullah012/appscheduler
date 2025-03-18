package com.meldcx.appscheduler.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.meldcx.appscheduler.presentation.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppListScreen() {
    val viewModel: AppViewModel = koinViewModel()
    val apps by viewModel.apps.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { viewModel.syncInstalledApps() }) {
            Text("Fetch Installed Apps")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(apps) { app ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "App Name: ${app.appName}", style = MaterialTheme.typography.titleLarge)
                        Text(text = "Package: ${app.packageName}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Version: ${app.versionName}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}