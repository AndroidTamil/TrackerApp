package com.example.trackerapp.service

// LocationForegroundService.kt
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.trackerapp.repository.LocationRepository
import com.example.trackerapp.viewModel.LocationViewModel
import android.os.Bundle
import android.util.Log

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class LocationForegroundService : Service() {

    private lateinit var locationManager: LocationManager
    private lateinit var viewModel: LocationViewModel

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel = LocationViewModel(LocationRepository())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60000,
                0f,
                locationListener
            )
        } catch (ex: SecurityException) {
            // Handle exception
        }

        createNotificationChannel()
        println("service activated")
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location Service")
            .setContentText("Collecting location data")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.storeLocation(
                    location.latitude,
                    location.longitude,
                    Calendar.getInstance().time
                )
                println("location:")
                Log.d("LocationForegroundService", "Location stored: $location")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        //override fun onProviderEnabled(provider: String?) {}
        //override fun onProviderDisabled(provider: String?) {}
    }

    companion object {
        private const val CHANNEL_ID = "LocationForegroundServiceChannel"
    }
}
