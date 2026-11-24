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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travelplannerapp.ui.theme.TravelPlannerAppTheme
import org.koin.androidx.compose.koinViewModel

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
    val listViewModel: ListViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            TravelScreen(
                listViewModel = listViewModel,
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
            val trip = id?.let { listViewModel.getTripById(it) }

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
fun TravelScreen(
    listViewModel: ListViewModel,
    onTripClick: (Int) -> Unit
) {
    val state by listViewModel.uiState.collectAsStateWithLifecycle()

    val filteredTrips = state.items.filter {
        it.name.contains(state.query, ignoreCase = true) ||
                it.destination.city.contains(state.query, ignoreCase = true) ||
                it.destination.country.contains(state.query, ignoreCase = true)
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.query,
                onValueChange = { listViewModel.updateSearch(it) },
                label = { Text("Пошук подорожі") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            Text("Знайдено подорожей: ${filteredTrips.size}")

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTrips) { trip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTripClick(trip.id)
                            },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = trip.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                "${trip.destination.country}, ${trip.destination.city}"
                            )

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
fun DetailScreen(
    trip: Trip,
    onBack: () -> Unit
) {
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
                text = trip.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Країна: ${trip.destination.country}")
                    Text("Місто: ${trip.destination.city}")
                    Text("Статус: ${trip.status.label()}")
                    Text("Бюджет: ${trip.budget.toInt()} €")
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Витрати",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(10.dp))

            trip.expenses.forEach { expense ->
                Text("${expense.title}: ${expense.amount.toInt()} €")
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Text("Всього витрачено: ${spent.toInt()} €")
            Text("Залишок бюджету: ${remaining.toInt()} €")

            if (trip.notes != null) {
                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Нотатки",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                Text(trip.notes)
            }
        }
    }
}