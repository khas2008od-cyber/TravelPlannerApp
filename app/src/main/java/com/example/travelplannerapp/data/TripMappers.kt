package com.example.travelplannerapp.data

import com.example.travelplannerapp.Destination
import com.example.travelplannerapp.Expense
import com.example.travelplannerapp.Trip
import com.example.travelplannerapp.TripStatus

fun CountryDto.toEntity(id: Int): TripEntity {
    val budgets = listOf(
        1500.0, 900.0, 400.0, 1300.0,
        250.0, 600.0, 700.0, 3000.0
    )

    val statuses = listOf(
        "PLANNED",
        "COMPLETED",
        "PLANNED",
        "IN_PROGRESS",
        "COMPLETED",
        "PLANNED",
        "COMPLETED",
        "PLANNED"
    )

    return TripEntity(
        id = id,
        name = "Подорож до ${name.common}",
        country = name.common,
        city = capital?.firstOrNull() ?: "Невідомо",
        region = region ?: "Невідомо",
        budget = budgets[id - 1],
        status = statuses[id - 1]
    )
}

fun TripEntity.toDomain(): Trip {
    return Trip(
        id = id,
        name = name,
        destination = Destination(country, city),
        budget = budget,
        status = TripStatus.valueOf(status),
        expenses = listOf(
            Expense("Транспорт", budget * 0.2),
            Expense("Готель", budget * 0.3)
        ),
        notes = "Регіон: $region"
    )
}