package com.example.taskflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    val description: String,

    val color: String,

    val createAt: Long,

    val updateAt: Long

)
