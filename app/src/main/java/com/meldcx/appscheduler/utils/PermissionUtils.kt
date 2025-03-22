package com.meldcx.appscheduler.utils

import android.Manifest.permission.SCHEDULE_EXACT_ALARM
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GrantPermission(onAllPermissionGranted: @Composable () -> Unit) {
    val permissions:MutableList<String> = mutableListOf()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        permissions.add(SCHEDULE_EXACT_ALARM)
    }
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    LaunchedEffect(Unit) {
        if (!multiplePermissionsState.allPermissionsGranted) {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    when {
        multiplePermissionsState.allPermissionsGranted -> {
            Log.i("TAG", "GrantPermission: All permissions granted!")
            onAllPermissionGranted()
        }
    }
}

fun canRequestManageOverlayPermission(context: Context): Boolean {
    return !Settings.canDrawOverlays(context)
}

fun requestManageOverlayPermission(context: Context){
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        ("package:" + context.packageName).toUri())
    context.startActivity(intent)
}

fun canRequestDisableBatteryOptimization(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = context.packageName
    return !powerManager.isIgnoringBatteryOptimizations(packageName)
}


fun requestDisableBatteryOptimization(context: Context, packageName:String){
    try {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = "package:$packageName".toUri()
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("BatteryOptimization", "Error requesting battery optimization", e)
    }
}
