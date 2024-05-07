package com.example.trackerapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.trackerapp.repository.LocationRepository
import java.util.*

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    fun storeLocation(latitude: Double, longitude: Double, timestamp: Date) {
        repository.storeLocation(latitude, longitude, timestamp)
    }

}