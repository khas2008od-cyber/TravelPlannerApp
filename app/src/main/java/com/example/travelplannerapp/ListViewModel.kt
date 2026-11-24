package com.example.travelplannerapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelplannerapp.data.TripRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListUiState(
    val items: List<Trip> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TripRepositoryImpl(application)

    private val _uiState = MutableStateFlow(ListUiState())

    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeTrips().collect { trips ->
                _uiState.update {
                    it.copy(items = trips)
                }
            }
        }

        refreshTrips()
    }

    fun refreshTrips() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val success = repository.refresh()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = if (success) {
                        null
                    } else {
                        "Немає з'єднання. Показано збережені дані"
                    }
                )
            }
        }
    }

    fun updateSearch(text: String) {
        _uiState.update {
            it.copy(query = text)
        }
    }

    fun getTripById(id: Int): Trip? {
        return _uiState.value.items.find { it.id == id }
    }
}