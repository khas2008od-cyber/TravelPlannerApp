package com.example.travelplannerapp.data

import com.example.travelplannerapp.Destination
import com.example.travelplannerapp.Expense
import com.example.travelplannerapp.Trip
import com.example.travelplannerapp.TripStatus
import com.example.travelplannerapp.domain.TripRepository
import kotlinx.coroutines.CancellationException

class TripRepositoryImpl : TripRepository {

    override suspend fun getTrips(): List<Trip> {
        return try {
            val countries = ApiClient.api.getCountries()

            val selected = listOf(
                "France",
                "Italy",
                "Czechia",
                "Spain",
                "Slovakia",
                "Germany",
                "Austria",
                "Japan"
            )

            val budgets = listOf(
                1500.0, 900.0, 400.0, 1300.0,
                250.0, 600.0, 700.0, 3000.0
            )

            val statuses = listOf(
                TripStatus.PLANNED,
                TripStatus.COMPLETED,
                TripStatus.PLANNED,
                TripStatus.IN_PROGRESS,
                TripStatus.COMPLETED,
                TripStatus.PLANNED,
                TripStatus.COMPLETED,
                TripStatus.PLANNED
            )

            selected.mapIndexedNotNull { index, countryName ->
                val country = countries.find {
                    it.name.common.equals(countryName, ignoreCase = true)
                } ?: return@mapIndexedNotNull null

                Trip(
                    id = index + 1,
                    name = "Подорож до ${country.name.common}",
                    destination = Destination(
                        country = country.name.common,
                        city = country.capital?.firstOrNull() ?: "Невідомо"
                    ),
                    budget = budgets[index],
                    status = statuses[index],
                    expenses = listOf(
                        Expense("Транспорт", budgets[index] * 0.2),
                        Expense("Готель", budgets[index] * 0.3)
                    ),
                    notes = "Регіон: ${country.region ?: "Невідомо"}"
                )
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("TravelPlanner", "Помилка завантаження країн", e)
            emptyList()
        }
    }
}