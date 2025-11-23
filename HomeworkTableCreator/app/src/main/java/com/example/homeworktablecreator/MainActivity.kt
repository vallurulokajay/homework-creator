package com.example.homeworktablecreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.homeworktablecreator.ui.theme.HomeworkTableCreatorTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    
    private val viewModel: HomeworkViewModel by viewModels()
    private lateinit var fileRepository: FileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileRepository = FileRepository(this)

        setContent {
            HomeworkTableCreatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeworkScreen(
                        viewModel = viewModel,
                        onGenerateClick = { generateImage() },
                        onShareClick = { uri -> fileRepository.shareImage(uri) }
                    )
                }
            }
        }
    }

    private fun generateImage() {
        val dateMillis = viewModel.selectedDate ?: return
        val dateString = "Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateMillis))}"
        
        val bitmap = TableImageGenerator.createTableBitmap(
            context = this,
            dateHeader = dateString,
            entries = viewModel.entries
        )
        
        val fileName = "Homework_${System.currentTimeMillis()}"
        val uri = fileRepository.saveBitmapToStorage(bitmap, fileName)
        
        if (uri != null) {
            viewModel.setGeneratedUri(uri)
        }
    }
}
