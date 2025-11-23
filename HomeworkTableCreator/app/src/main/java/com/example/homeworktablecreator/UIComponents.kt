package com.example.homeworktablecreator

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.res.painterResource
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    viewModel: HomeworkViewModel,
    onGenerateClick: () -> Unit,
    onShareClick: (Uri) -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Observe ViewModel state
    val uiMessage = viewModel.uiMessage
    
    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUiMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Homework Table Creator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Date Selection
            DateSection(
                selectedDate = viewModel.selectedDate,
                onDateSelected = { viewModel.setDate(it) }
            )

            Divider()

            // 2. Entry Form
            EntryForm(
                onAddEntry = { sub, hw -> viewModel.addEntry(sub, hw) },
                isEnabled = viewModel.selectedDate != null
            )

            Divider()

            // 3. List of Entries
            Text(
                "Entries (${viewModel.entries.size})",
                style = MaterialTheme.typography.titleMedium
            )
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.entries) { entry ->
                    EntryItem(
                        entry = entry,
                        onDelete = { viewModel.removeEntry(it) }
                    )
                }
            }

            // 4. Action Buttons
            Button(
                onClick = onGenerateClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.entries.isNotEmpty() && viewModel.selectedDate != null
            ) {
                Text("Create Table Image")
            }

            // 5. Credits
            CreditSection()
        }
    }

    // Preview Dialog
    viewModel.generatedImageUri?.let { uri ->
        PreviewDialog(
            imageUri = uri,
            onDismiss = { viewModel.setGeneratedUri(Uri.EMPTY) },
            onShare = { onShareClick(uri) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSection(selectedDate: Long?, onDateSelected: (Long) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (selectedDate != null) {
                "Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDate))}"
            } else {
                "Select Date (Required)"
            },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { showDatePicker = true }) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Date")
        }
    }
}

@Composable
fun EntryForm(onAddEntry: (String, String) -> Unit, isEnabled: Boolean) {
    var subject by remember { mutableStateOf("") }
    var homework by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text("Subject") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEnabled,
            singleLine = true
        )
        OutlinedTextField(
            value = homework,
            onValueChange = { homework = it },
            label = { Text("Homework") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEnabled,
            minLines = 2,
            maxLines = 4
        )
        Button(
            onClick = {
                onAddEntry(subject, homework)
                subject = ""
                homework = ""
            },
            modifier = Modifier.align(Alignment.End),
            enabled = isEnabled && subject.isNotBlank() && homework.isNotBlank()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text("Add Entry")
        }
    }
}

@Composable
fun EntryItem(entry: HomeworkEntry, onDelete: (HomeworkEntry) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.subject,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = entry.homework,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { onDelete(entry) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun PreviewDialog(imageUri: Uri, onDismiss: () -> Unit, onShare: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Preview", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.LightGray)
                ) {
                     Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Generated Table",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
                
                Spacer(Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Close")
                    }
                    Button(onClick = onShare) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Share")
                    }
                }
            }
        }
    }
}

@Composable
fun CreditSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Developed by LOK AJAY",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
