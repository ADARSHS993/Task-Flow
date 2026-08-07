package com.example.taskflow.data.mapper

import com.example.taskflow.data.local.entity.CategoryEntity
import com.example.taskflow.data.local.entity.TaskEntity
import com.example.taskflow.domain.model.Category
import com.example.taskflow.domain.model.Priority
import com.example.taskflow.domain.model.Task

fun TaskEntity.toDomain(): Task{
    return Task(
        id = id,
        title = title,
        description = description,
        priority = Priority.valueOf(priority),
        dueDate = dueDate,
        isCompleted = isCompleted,
        projectId = projectId,
        categoryId = categoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CategoryEntity.toDomain(): Category{
    return Category(
        id = id,
        name = name,
        color = color,
        icon = icon
    )
}

fun Category.toEntity(): CategoryEntity{
    return CategoryEntity(
        id = id,
        name = name,
        color = color,
        icon = icon
    )
}

fun Task.toEntity(): TaskEntity {

    return TaskEntity(
        id = id,
        title = title,
        description = description,
        priority = priority.name,
        dueDate = dueDate,
        isCompleted = isCompleted,
        projectId = projectId,
        categoryId = categoryId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}