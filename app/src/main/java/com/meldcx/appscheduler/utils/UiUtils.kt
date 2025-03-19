package com.meldcx.appscheduler.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.meldcx.appscheduler.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun getIconDrawableByPackageName(context: Context, packageName: String) : ImageBitmap {
    val packageManager = context.packageManager

    val iconDrawable = remember {
        try {
            packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    if (iconDrawable != null) {
        return iconDrawable.toBitmap().asImageBitmap()
    }
    return AppCompatResources.getDrawable(context, R.mipmap.ic_launcher)!!.toBitmap().asImageBitmap()
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Customize the format as needed
    val date = Date(timestamp)
    return sdf.format(date)
}