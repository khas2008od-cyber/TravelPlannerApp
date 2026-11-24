package com.example.travelplannerapp.domain

import com.example.travelplannerapp.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    suspend fun getTrips(): List<Trip>
    fun observeTrips(): Flow<List<Trip>>
    suspend fun refresh(): Boolean
}