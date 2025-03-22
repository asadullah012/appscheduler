package com.meldcx.appscheduler.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.meldcx.appscheduler.utils.DatePickerModal
import com.meldcx.appscheduler.utils.TimePickerModal
import com.meldcx.appscheduler.utils.formatTimestamp
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    title:String,
    reschedule:Boolean = false,
    scheduledTime: Long? = null,
    showBottomSheet: Boolean,
    onSchedule: (LocalDateTime?) -> Unit,
    onCancelSchedule: () -> Unit = {},
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    fun dismissBottomSheet() {
        scope.launch { sheetState.hide() }
        onDismissRequest()
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { dismissBottomSheet() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                if(reschedule){
                    Text(
                        "Existing Schedule: ${formatTimestamp(scheduledTime!!)}",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedDate?.toString() ?: "Pick a Date")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedTime?.toString() ?: "Pick a Time")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val selectedDateTime = LocalDateTime.of(selectedDate, selectedTime)
                        val currentDateTime = LocalDateTime.now()
                        if(selectedDateTime.isBefore(currentDateTime)){
                            Toast.makeText(context, "Selected date and time must be in the future", Toast.LENGTH_SHORT).show()
                        } else {
                            onSchedule(selectedDateTime)
                            dismissBottomSheet()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedDate != null && selectedTime != null
                ) {
                    Text("Schedule App Launch")
                }

                Spacer(modifier = Modifier.height(8.dp))

                if(reschedule){
                    Button(
                        onClick = {
                            onCancelSchedule()
                            dismissBottomSheet()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel Schedule")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = { dismissBottomSheet() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it?.let { millis ->
                    LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                }
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }

    if (showTimePicker) {
        TimePickerModal(
            onTimeSelected = {
                selectedTime = it
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
}