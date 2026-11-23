package com.example.travelplannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    Trip(1, "Відпочинок у Парижі",
        Destination("Франція", "Париж"), 1500.0,
        TripStatus.PLANNED,
        listOf(Expense("Готель", 500.0), Expense("Квитки", 300.0)),
        "Відвідати Ейфелеву вежу"),

    Trip(2, "Подорож до Рима",
        Destination("Італія", "Рим"), 900.0,
        TripStatus.COMPLETED,
        listOf(Expense("Готель", 350.0), Expense("Їжа", 200.0))),

    Trip(3, "Вікенд у Празі",
        Destination("Чехія", "Прага"), 400.0,
        TripStatus.PLANNED,
        listOf(Expense("Транспорт", 100.0), Expense("Готель", 150.0))),

    Trip(4, "Море в Барселоні",
        Destination("Іспанія", "Барселона"), 1300.0,
        TripStatus.IN_PROGRESS,
        listOf(Expense("Квитки", 400.0), Expense("Готель", 450.0))),

    Trip(5, "Гори у Словаччині",
        Destination("Словаччина", "Татри"), 250.0,
        TripStatus.COMPLETED,
        listOf(Expense("Транспорт", 50.0), Expense("Проживання", 100.0))),

    Trip(6, "Подорож до Берліна",
        Destination("Німеччина", "Берлін"), 600.0,
        TripStatus.PLANNED,
        listOf(Expense("Квитки", 150.0), Expense("Готель", 250.0))),

    Trip(7, "Відпочинок у Відні",
        Destination("Австрія", "Відень"), 700.0,
        TripStatus.COMPLETED,
        listOf(Expense("Транспорт", 120.0), Expense("Готель", 300.0))),

    Trip(8, "Подорож до Токіо",
        Destination("Японія", "Токіо"), 3000.0,
        TripStatus.PLANNED,
        listOf(Expense("Авіаквитки", 1000.0), Expense("Готель", 900.0)),
        "Відвідати район Сібуя")
)

fun TripStatus.label(): String {
    return when (this) {
        TripStatus.PLANNED -> "Заплановано"
        TripStatus.IN_PROGRESS -> "У процесі"
        TripStatus.COMPLETED -> "Завершено"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TravelPlannerAppTheme {
                TravelApp()
            }
        }
    }
}

@Composable
fun TravelApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            TravelScreen(
                onTripClick = { id ->
                    navController.navigate("details/$id")
                }
            )
        }

        composable(
            route = "details/{itemId}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("itemId")
            val trip = trips.find { it.id == id }

            if (trip != null) {
                DetailScreen(
                    trip = trip,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun TravelScreen(onTripClick: (Int) -> Unit) {
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
                "Планувальник подорожей",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Пошук подорожі") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Text("Знайдено подорожей: ${filteredTrips.size}")

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTrips) { trip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTripClick(trip.id) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                trip.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(6.dp))

                            Text("${trip.destination.country}, ${trip.destination.city}")
                            Text("Бюджет: ${trip.budget.toInt()} €")
                            Text("Статус: ${trip.status.label()}")
                            Text("Натисніть для деталей")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(trip: Trip, onBack: () -> Unit) {
    val spent = trip.expenses.sumOf { it.amount }
    val remaining = trip.budget - spent

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                TextButton(onClick = onBack) {
                    Text("← Назад")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                trip.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Країна: ${trip.destination.country}")
                    Text("Місто: ${trip.destination.city}")
                    Text("Статус: ${trip.status.label()}")
                    Text("Бюджет: ${trip.budget.toInt()} €")
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Витрати",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(10.dp))

            trip.expenses.forEach { expense ->
                Text("${expense.title}: ${expense.amount.toInt()} €")
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text("Всього витрачено: ${spent.toInt()} €")
            Text("Залишок бюджету: ${remaining.toInt()} €")

            if (trip.notes != null) {
                Spacer(Modifier.height(20.dp))

                Text(
                    "Нотатки",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                Text(trip.notes)
            }
        }
    }
}