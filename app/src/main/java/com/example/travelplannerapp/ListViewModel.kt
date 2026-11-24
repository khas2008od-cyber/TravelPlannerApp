package com.example.travelplannerapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ListUiState(
    val items: List<Trip> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false
)

class ListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ListUiState(
            items = listOf(
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
        )
    )

    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    fun updateSearch(text: String) {
        _uiState.update { it.copy(query = text) }
    }

    fun getTripById(id: Int): Trip? {
        return _uiState.value.items.find { it.id == id }
    }
}