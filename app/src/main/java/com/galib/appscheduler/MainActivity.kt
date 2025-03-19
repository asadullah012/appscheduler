package com.galib.appscheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.galib.appscheduler.presentation.screen.AppListScreen
import com.galib.appscheduler.presentation.screen.MainScreen
import com.galib.appscheduler.utils.GrantPermission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}