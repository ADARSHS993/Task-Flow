package com.example.taskflow.domain.model

data class Category(
    var id: String = "",

    val name: String,

    val createAt: Long,

    val updateAt: Long
)
