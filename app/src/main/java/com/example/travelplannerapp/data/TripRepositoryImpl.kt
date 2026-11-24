package com.example.travelplannerapp.data

import com.example.travelplannerapp.Destination
import com.example.travelplannerapp.Expense
import com.example.travelplannerapp.Trip
import com.example.travelplannerapp.TripStatus
import com.example.travelplannerapp.domain.TripRepository

class TripRepositoryImpl : TripRepository {

    override suspend fun getTrips(): List<Trip> {
        return listOf(
            Trip(
                1, "Відпочинок у Парижі",
                Destination("Франція", "Париж"),
                1500.0, TripStatus.PLANNED,
                listOf(
                    Expense("Готель", 500.0),
                    Expense("Квитки", 300.0)
                ),
                "Відвідати Ейфелеву вежу"
            ),

            Trip(
                2, "Подорож до Рима",
                Destination("Італія", "Рим"),
                900.0, TripStatus.COMPLETED,
                listOf(
                    Expense("Готель", 350.0),
                    Expense("Їжа", 200.0)
                )
            ),

            Trip(
                3, "Вікенд у Празі",
                Destination("Чехія", "Прага"),
                400.0, TripStatus.PLANNED,
                listOf(
                    Expense("Транспорт", 100.0),
                    Expense("Готель", 150.0)
                )
            ),

            Trip(
                4, "Море в Барселоні",
                Destination("Іспанія", "Барселона"),
                1300.0, TripStatus.IN_PROGRESS,
                listOf(
                    Expense("Квитки", 400.0),
                    Expense("Готель", 450.0)
                )
            ),

            Trip(
                5, "Гори у Словаччині",
                Destination("Словаччина", "Татри"),
                250.0, TripStatus.COMPLETED,
                listOf(
                    Expense("Транспорт", 50.0),
                    Expense("Проживання", 100.0)
                )
            ),

            Trip(
                6, "Подорож до Берліна",
                Destination("Німеччина", "Берлін"),
                600.0, TripStatus.PLANNED,
                listOf(
                    Expense("Квитки", 150.0),
                    Expense("Готель", 250.0)
                )
            ),

            Trip(
                7, "Відпочинок у Відні",
                Destination("Австрія", "Відень"),
                700.0, TripStatus.COMPLETED,
                listOf(
                    Expense("Транспорт", 120.0),
                    Expense("Готель", 300.0)
                )
            ),

            Trip(
                8, "Подорож до Токіо",
                Destination("Японія", "Токіо"),
                3000.0, TripStatus.PLANNED,
                listOf(
                    Expense("Авіаквитки", 1000.0),
                    Expense("Готель", 900.0)
                ),
                "Відвідати район Сібуя"
            )
        )
    }
}