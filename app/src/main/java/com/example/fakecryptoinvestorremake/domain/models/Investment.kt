package com.example.fakecryptoinvestorremake.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Investment(
    val name: String,
    val value: Int,
    val exchangeRate: Double,
    val hypothesis: String,
    val dateOfCreation: Long,
    val profit: Double,
    @PrimaryKey val id: Int? = null
)

class InvalidInvestmentException(message: String): Exception(message)
