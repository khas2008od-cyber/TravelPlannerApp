package com.example.travelplannerapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelplannerapp.data.TripRepositoryImpl
import com.example.travelplannerapp.domain.GetTripsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListUiState(
    val items: List<Trip> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false
)

class ListViewModel : ViewModel() {

    private val getTripsUseCase = GetTripsUseCase(TripRepositoryImpl())

    private val _uiState = MutableStateFlow(ListUiState())

    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val trips = getTripsUseCase()

                _uiState.update {
                    it.copy(
                        items = trips,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false)
                }
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