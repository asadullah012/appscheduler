package com.meldcx.appscheduler.presentation.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.meldcx.appscheduler.utils.canRequestDisableBatteryOptimization
import com.meldcx.appscheduler.utils.canRequestManageOverlayPermission
import com.meldcx.appscheduler.utils.requestDisableBatteryOptimization
import com.meldcx.appscheduler.utils.requestManageOverlayPermission
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val action: () -> Unit,
    val shouldShowAction: () -> Boolean
)

fun getOnboardingPages(context: Context): List<OnboardingPage> {
    val pages = mutableListOf<OnboardingPage>()
    if(canRequestManageOverlayPermission(context)){
        pages.add(OnboardingPage(
            "Allow Display over other apps",
            "To launch app in background, allow display over other apps",
            {requestManageOverlayPermission(context)},
            {canRequestManageOverlayPermission(context)}
        ))
    }

    if(canRequestDisableBatteryOptimization(context)){
        pages.add(OnboardingPage(
            "Disable Battery Optimization",
            "To launch app properly in time, please disable battery optimization",
            { requestDisableBatteryOptimization(context, context.packageName) },
            { canRequestDisableBatteryOptimization(context) }
        ))
    }
    return pages
}


@Composable
fun PermissionDialog(context: Context) {
    var showDialog by remember { mutableStateOf(shouldShowOnboarding(context)) }
    if (showDialog) {
        PermissionOnboardingDialog(context = context) {
            showDialog = false
        }
    }
}

fun shouldShowOnboarding(context: Context): Boolean {
    return getOnboardingPages(context).isNotEmpty()
}

@Composable
fun PermissionOnboardingDialog(context: Context, onDismiss: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val pages = getOnboardingPages(context)
    val pagerState = rememberPagerState(pageCount = {pages.size})

    fun onNextClick(){
        coroutineScope.launch {
            if (pagerState.currentPage == pages.size - 1) {
                onDismiss() // Finish button closes the dialog
            } else {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }

    fun onPreviousClick(){
        coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
    }


    if (pages.isNotEmpty()) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalPager( state = pagerState) { page ->
                        val onboardingPage = pages[page]
                        PermissionPage(
                            title = onboardingPage.title,
                            description = onboardingPage.description,
                            onGrantClick = {
                                onboardingPage.action()
                                onNextClick() },
                            canGrant = {onboardingPage.shouldShowAction()}
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onPreviousClick() },
                            enabled = pagerState.currentPage > 0
                        ) {
                            Text("Previous")
                        }

                        Button(
                            onClick = {
                                onNextClick()
                            }
                        ) {
                            Text(if (pagerState.currentPage == pages.size - 1) "Finish" else "Next")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionPage(title: String, description: String, onGrantClick: () -> Unit, canGrant: () -> Boolean) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        if(canGrant()){
            Button(onClick = { onGrantClick() }) {
                Text("Grant Permission")
            }
        } else {
            Text("Already Granted Permission")
        }
    }
}


