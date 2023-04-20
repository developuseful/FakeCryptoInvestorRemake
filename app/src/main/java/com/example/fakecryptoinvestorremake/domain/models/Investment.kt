package com.example.fakecryptoinvestorremake.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Investment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val value: Double,
    val hypothesis: String,
    val dateOfCreation: Long,
    val profit: Double
)
