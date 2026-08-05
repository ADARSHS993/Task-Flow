package com.example.taskflow.domain.model

data class Task(
    val id: String = "",
    val title: String ,
    val description: String,
    val priority: Priority,
    val dueDate: Long?,
    val isCompleted: Boolean = false,
    val projectId: String? = null,
    val categoryId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class Priority{
    LOW,
    MEDIUM,
    HIGH
}
