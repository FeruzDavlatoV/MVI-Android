package com.example.mvi.model

import java.io.Serializable

data class Post(
    val id: Int? = null,
    val userId: Int? = null,
    val title: String? = null,
    val body: String? = null
): Serializable