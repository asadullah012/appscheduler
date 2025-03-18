package com.meldcx.appscheduler.presentation.screen

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.meldcx.appscheduler.R
import com.meldcx.appscheduler.domain.model.AppInfo
import com.meldcx.appscheduler.domain.model.LaunchSchedule
import com.meldcx.appscheduler.domain.model.SCHEDULE_STATUS
import com.meldcx.appscheduler.presentation.viewmodel.AppViewModel
import com.meldcx.appscheduler.presentation.viewmodel.LaunchScheduleViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppListScreen() {
    val appViewModel: AppViewModel = koinViewModel()
    val apps by appViewModel.apps.collectAsState()
    val launchScheduleViewModel: LaunchScheduleViewModel = koinViewModel()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 100.dp)
        ) {
            items(apps) { app ->
                InstalledAppItem(app, onAppClick = {
                    launchScheduleViewModel.insertLaunchSchedule(
                        LaunchSchedule(
                            packageName = app.packageName,
                            scheduledTime = System.currentTimeMillis(),
                            status = SCHEDULE_STATUS.SCHEDULED
                        )
                    )
                })
            }
        }
    }
}

@Composable
fun InstalledAppItem(app: AppInfo, onAppClick: () -> Unit){
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        onClick = {
            //openApp(context, app.packageName)
            onAppClick()
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
}

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

fun openApp(context: Context, packageName: String) {
    val intent = context.packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show()
    }
}
