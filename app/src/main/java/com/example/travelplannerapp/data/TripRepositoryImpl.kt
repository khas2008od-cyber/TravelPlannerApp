package com.example.travelplannerapp.data

import android.content.Context
import android.util.Log
import com.example.travelplannerapp.Trip
import com.example.travelplannerapp.domain.TripRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TripRepositoryImpl(context: Context) : TripRepository {

    private val dao = AppDatabase.getInstance(context).tripDao()

    override fun observeTrips(): Flow<List<Trip>> {
        return dao.getAll().map { savedTrips ->
            savedTrips.map { it.toDomain() }
        }
    }

    override suspend fun getTrips(): List<Trip> {
        return observeTrips().first()
    }

    override suspend fun refresh(): Boolean {
        return try {
            val countries = ApiClient.api.getCountries()

            val selectedCountries = listOf(
                "France",
                "Italy",
                "Czechia",
                "Spain",
                "Slovakia",
                "Germany",
                "Austria",
                "Japan"
            )

            val trips = selectedCountries.mapIndexedNotNull { index, countryName ->
                val country = countries.find {
                    it.name.common.equals(countryName, ignoreCase = true)
                }

                country?.toEntity(index + 1)
            }

            if (trips.isNotEmpty()) {
                dao.insertAll(trips)
                true
            } else {
                false
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("TravelPlanner", "Помилка синхронізації", e)
            false
        }
    }
}