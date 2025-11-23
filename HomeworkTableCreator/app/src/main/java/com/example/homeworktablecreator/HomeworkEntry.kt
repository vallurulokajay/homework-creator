package com.example.homeworktablecreator

import java.util.UUID

data class HomeworkEntry(
    val id: String = UUID.randomUUID().toString(),
    val subject: String,
    val homework: String
)
