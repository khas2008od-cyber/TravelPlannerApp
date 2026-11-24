package com.example.travelplannerapp.domain

class GetTripsUseCase(
    private val repository: TripRepository
) {
    suspend operator fun invoke() = repository.getTrips()
}