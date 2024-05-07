package com.example.trackerapp.model

import io.realm.RealmObject
import java.util.Date

open class LocationData: RealmObject() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var timestamp: Date = Date()

}