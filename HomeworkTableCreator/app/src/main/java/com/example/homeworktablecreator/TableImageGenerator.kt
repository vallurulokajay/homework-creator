package com.example.homeworktablecreator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

object TableImageGenerator {

    fun createTableBitmap(
        context: Context,
        dateHeader: String,
        entries: List<HomeworkEntry>,
        widthPx: Int = 1200
    ): Bitmap {
        // 1. Setup Paints
        val headerPaint = TextPaint().apply {
            color = Color.BLACK
            textSize = 60f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val columnHeaderPaint = TextPaint().apply {
            color = Color.DKGRAY
            textSize = 48f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }

        val contentPaint = TextPaint().apply {
            color = Color.BLACK
            textSize = 40f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }

        val linePaint = Paint().apply {
            color = Color.GRAY
            strokeWidth = 3f
            style = Paint.Style.STROKE
        }

        // 2. Calculate Dimensions
        val padding = 40
        val col1Width = (widthPx - 2 * padding) * 0.3f // 30% for Subject
        val col2Width = (widthPx - 2 * padding) * 0.7f // 70% for Homework
        val rowPadding = 20

        // Calculate header height
        val headerLayout = StaticLayout.Builder.obtain(dateHeader, 0, dateHeader.length, headerPaint, widthPx - 2 * padding)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .build()
        val headerHeight = headerLayout.height + 2 * padding

        // Calculate table header height
        val subHeaderLayout = StaticLayout.Builder.obtain("Subject", 0, 7, columnHeaderPaint, col1Width.toInt())
            .build()
        val hwHeaderLayout = StaticLayout.Builder.obtain("Homework", 0, 8, columnHeaderPaint, col2Width.toInt())
            .build()
        val tableHeaderHeight = maxOf(subHeaderLayout.height, hwHeaderLayout.height) + 2 * rowPadding

        // Calculate rows height
        val rowHeights = entries.map { entry ->
            val subLayout = StaticLayout.Builder.obtain(entry.subject, 0, entry.subject.length, contentPaint, col1Width.toInt()).build()
            val hwLayout = StaticLayout.Builder.obtain(entry.homework, 0, entry.homework.length, contentPaint, col2Width.toInt()).build()
            maxOf(subLayout.height, hwLayout.height) + 2 * rowPadding
        }

        val totalHeight = headerHeight + tableHeaderHeight + rowHeights.sum() + padding

        // 3. Create Bitmap and Canvas
        val bitmap = Bitmap.createBitmap(widthPx, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // 4. Draw Header
        canvas.save()
        canvas.translate(padding.toFloat(), padding.toFloat())
        headerLayout.draw(canvas)
        canvas.restore()

        // Draw Separator Line under main header
        var currentY = headerHeight.toFloat()
        canvas.drawLine(padding.toFloat(), currentY, (widthPx - padding).toFloat(), currentY, linePaint)

        // 5. Draw Table Headers
        canvas.save()
        canvas.translate(padding.toFloat(), currentY + rowPadding)
        subHeaderLayout.draw(canvas)
        canvas.translate(col1Width, 0f)
        hwHeaderLayout.draw(canvas)
        canvas.restore()

        currentY += tableHeaderHeight
        canvas.drawLine(padding.toFloat(), currentY, (widthPx - padding).toFloat(), currentY, linePaint)

        // 6. Draw Rows
        entries.forEachIndexed { index, entry ->
            val rowHeight = rowHeights[index]
            
            canvas.save()
            canvas.translate(padding.toFloat(), currentY + rowPadding)
            
            // Draw Subject
            val subLayout = StaticLayout.Builder.obtain(entry.subject, 0, entry.subject.length, contentPaint, col1Width.toInt()).build()
            subLayout.draw(canvas)
            
            // Draw Homework
            canvas.translate(col1Width, 0f)
            val hwLayout = StaticLayout.Builder.obtain(entry.homework, 0, entry.homework.length, contentPaint, col2Width.toInt()).build()
            hwLayout.draw(canvas)
            
            canvas.restore()

            currentY += rowHeight
            // Draw line after each row
            canvas.drawLine(padding.toFloat(), currentY, (widthPx - padding).toFloat(), currentY, linePaint)
        }

        // Draw Vertical Line separating columns
        val tableStartY = headerHeight.toFloat()
        val verticalLineX = padding + col1Width
        canvas.drawLine(verticalLineX, tableStartY, verticalLineX, currentY, linePaint)

        return bitmap
    }
    
    private fun maxOf(a: Int, b: Int): Int = if (a > b) a else b
}
