package com.example.travelplannerapp.domain

import com.example.travelplannerapp.Trip

interface TripRepository {
    suspend fun getTrips(): List<Trip>
}