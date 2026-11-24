package com.example.travelplannerapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String,
    val city: String,
    val region: String,
    val budget: Double,
    val status: String
)