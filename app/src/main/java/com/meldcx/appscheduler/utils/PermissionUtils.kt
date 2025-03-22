package com.meldcx.appscheduler.utils

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.core.net.toUri
import com.meldcx.appscheduler.R

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
        Log.e("BatteryOptimization",
            context.getString(R.string.error_requesting_battery_optimization), e)
    }
}
