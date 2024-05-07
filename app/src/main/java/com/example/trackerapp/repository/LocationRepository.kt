package com.example.trackerapp.repository

// LocationRepository.kt
import com.example.trackerapp.model.LocationData
import io.realm.Realm
import java.util.*

class LocationRepository {

    fun storeLocation(latitude: Double, longitude: Double, timestamp: Date) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { realm ->
                realm.createObject(LocationData::class.java).apply {
                    this.latitude = latitude
                    this.longitude = longitude
                    this.timestamp = timestamp
                }
            }
        }
    }




}
