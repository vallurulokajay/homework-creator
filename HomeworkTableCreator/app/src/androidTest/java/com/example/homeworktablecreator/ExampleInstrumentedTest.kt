package com.example.homeworktablecreator

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.homeworktablecreator", appContext.packageName)
    }

    @Test
    fun testBitmapCreation() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val entries = listOf(
            HomeworkEntry(subject = "Math", homework = "Page 10"),
            HomeworkEntry(subject = "Science", homework = "Read Chapter 3")
        )
        
        // This should not throw an exception
        val bitmap = TableImageGenerator.createTableBitmap(
            context = appContext,
            dateHeader = "Date: 2025-11-23",
            entries = entries
        )
        
        assertNotNull(bitmap)
        assertTrue(bitmap.width > 0)
        assertTrue(bitmap.height > 0)
    }
}
