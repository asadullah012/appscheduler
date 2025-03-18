package com.meldcx.appscheduler.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppScheduleScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { }) {
            Text("This is app schedule screen")
        }
    }
}