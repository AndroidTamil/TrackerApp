package com.example.trackerapp.repository

import com.example.trackerapp.model.LocationData
import io.realm.Realm

class DataRepository(private val realm: Realm) {

    fun getLocationData(): List<LocationData> {
        val results = realm.where(LocationData::class.java).findAll()
        return results.toList()
    }


}
