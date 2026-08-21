package com.example.taskflow.domain.model

data class Project(
    val id: String = "",
    val name: String,
    val description: String = "",
    val color: String = "",
    val createAt : Long = System.currentTimeMillis(),
    val updateAt : Long = System.currentTimeMillis()
)
