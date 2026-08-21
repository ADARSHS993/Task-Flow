package com.example.taskflow.data.mapper

import com.example.taskflow.data.local.entity.ProjectEntity
import com.example.taskflow.domain.model.Project

fun ProjectEntity.toDomain(): Project {
    return Project(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        createAt = this.createAt,
        updateAt = this.updateAt
    )
}

fun Project.toEntity(): ProjectEntity {
    return ProjectEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        createAt = this.createAt,
        updateAt = this.updateAt
    )
}