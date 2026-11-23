package com.example.travelplannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelplannerapp.ui.theme.TravelPlannerAppTheme

enum class TripStatus {
    PLANNED, IN_PROGRESS, COMPLETED
}

data class Destination(
    val country: String,
    val city: String
)

data class Expense(
    val title: String,
    val amount: Double
)

data class Trip(
    val id: Int,
    val name: String,
    val destination: Destination,
    val budget: Double,
    val status: TripStatus,
    val expenses: List<Expense>,
    val notes: String? = null
)

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

val trips = listOf(
    Trip(1, "Відпочинок у Парижі", Destination("Франція", "Париж"),
        1500.0, TripStatus.PLANNED, listOf(Expense("Готель", 500.0))),

    Trip(2, "Подорож до Рима", Destination("Італія", "Рим"),
        900.0, TripStatus.COMPLETED, listOf(Expense("Готель", 350.0))),

    Trip(3, "Вікенд у Празі", Destination("Чехія", "Прага"),
        400.0, TripStatus.PLANNED, listOf(Expense("Транспорт", 100.0))),

    Trip(4, "Море в Барселоні", Destination("Іспанія", "Барселона"),
        1300.0, TripStatus.IN_PROGRESS, listOf(Expense("Квитки", 400.0))),

    Trip(5, "Гори у Словаччині", Destination("Словаччина", "Татри"),
        250.0, TripStatus.COMPLETED, listOf(Expense("Транспорт", 50.0))),

    Trip(6, "Подорож до Берліна", Destination("Німеччина", "Берлін"),
        600.0, TripStatus.PLANNED, listOf(Expense("Квитки", 150.0))),

    Trip(7, "Відпочинок у Відні", Destination("Австрія", "Відень"),
        700.0, TripStatus.COMPLETED, listOf(Expense("Готель", 300.0))),

    Trip(8, "Подорож до Токіо", Destination("Японія", "Токіо"),
        3000.0, TripStatus.PLANNED, listOf(Expense("Квитки", 1000.0)))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TravelPlannerAppTheme {
                TravelScreen()
            }
        }
    }
}

@Composable
fun TravelScreen() {
    var search by remember { mutableStateOf("") }

    val filteredTrips = trips.filter {
        it.name.contains(search, ignoreCase = true) ||
                it.destination.city.contains(search, ignoreCase = true) ||
                it.destination.country.contains(search, ignoreCase = true)
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Планувальник подорожей",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Пошук подорожі") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Знайдено подорожей: ${filteredTrips.size}")

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTrips) { trip ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = trip.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("${trip.destination.country}, ${trip.destination.city}")
                            Text("Бюджет: ${trip.budget.toInt()} €")

                            val status = when (trip.status) {
                                TripStatus.PLANNED -> "Заплановано"
                                TripStatus.IN_PROGRESS -> "У процесі"
                                TripStatus.COMPLETED -> "Завершено"
                            }

                            Text("Статус: $status")
                        }
                    }
                }
            }
        }
    }
}