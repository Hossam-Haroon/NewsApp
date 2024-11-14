package com.example.newsapp.ui.models

import androidx.room.Entity
import java.io.Serializable
@Entity
data class Source(
    val id: String,
    val name: String
)