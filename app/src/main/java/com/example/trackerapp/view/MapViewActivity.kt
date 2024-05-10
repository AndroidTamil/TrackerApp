package com.example.trackerapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.trackerapp.R
import com.example.trackerapp.model.LocationData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import java.util.*

class MapViewActivity : AppCompatActivity() {
    private lateinit var locations: List<LocationData>
    private lateinit var bikeImageView: ImageView
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)

        // Get references to views
        val fab: FloatingActionButton = findViewById(R.id.fab)
        bikeImageView = findViewById(R.id.bikeImageView)

        // Retrieve locations from Realm database based on timestamp
        val timestamp: Long = intent.getLongExtra("timestamp", Long.MIN_VALUE)
        val realm = Realm.getDefaultInstance()
        locations = realm.where(LocationData::class.java)
            .greaterThan("timestamp", Date(timestamp))
            .findAll()

        // Display map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            // Draw polylines between locations
            val polylineOptions = PolylineOptions()
            locations.forEach { location ->
                polylineOptions.add(LatLng(location.latitude, location.longitude))
            }
            googleMap.addPolyline(polylineOptions)

            // Move camera to the first location
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        locations.first().latitude,
                        locations.first().longitude
                    ), 100f
                )
            )
            val firstLocation = LatLng(locations.first().latitude, locations.first().longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 100f))
            bikeImageView.visibility = View.VISIBLE

            // Iterate through locations and move bike image to each location
            locations.forEachIndexed { index, locationData ->
                val latLng = LatLng(locations.first().latitude, locations.first().longitude)
                moveBikeToLocation(bikeImageView, latLng, index * 3000L)


                // Start animating movement


            }

            // Set FAB click listener
            fab.setOnClickListener {
                // Ensure bike image is visible
                // Adjust duration as needed
                animateMovement(googleMap, bikeImageView)
            }
        }
    }

    private fun moveBikeToLocation(bikeImageView: ImageView, latLng: LatLng, delay: Long) {
        // Animate the translation of the bike image to the specified location
        bikeImageView.animate()
            .translationX(latLng.longitude.toFloat())
            .translationY(latLng.latitude.toFloat())
            .setStartDelay(delay)
            .start()
    }

    private fun animateMovement(googleMap: GoogleMap, bikeImageView: ImageView) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentIndex < locations.size - 1) {
                    val startLatLng = LatLng(
                        locations[currentIndex].latitude,
                        locations[currentIndex].longitude
                    )
                    val endLatLng = LatLng(
                        locations[currentIndex + 1].latitude,
                        locations[currentIndex + 1].longitude
                    )

                    // Move camera to the next location
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(endLatLng, 100f),
                        3000, // Adjust duration as needed
                        object : GoogleMap.CancelableCallback {
                            override fun onFinish() {
                                // Update position of the image after camera animation finishes
                                moveBikeToLocation(googleMap, bikeImageView, endLatLng)
                            }

                            override fun onCancel() {
                                // Handle cancellation if needed
                            }
                        }
                    )

                    // Increment index
                    currentIndex++

                    // Call this runnable again after a delay
                    handler.postDelayed(this, 3000) // Adjust delay as needed
                }
            }
        }, 3000) // Start animation after a delay
    }

    private fun moveBikeToLocation(googleMap: GoogleMap, bikeImageView: ImageView, latLng: LatLng) {
        // Animate the translation of the bike image to the specified location
        val mapView = findViewById<View>(R.id.map) // Assuming the map is contained in a view with id 'map'
        val mapWidth = mapView.width
        val mapHeight = mapView.height

        val latLngBounds = googleMap.projection.visibleRegion.latLngBounds
        val northEast = latLngBounds.northeast
        val southWest = latLngBounds.southwest

        val x = (mapWidth * (latLng.longitude - southWest.longitude) / (northEast.longitude - southWest.longitude)).toFloat()
        val y = (mapHeight * (1 - (latLng.latitude - southWest.latitude) / (northEast.latitude - southWest.latitude))).toFloat()

        // Animate the translation of the bike image to the specified location
        bikeImageView.animate()
            .translationX(x)
            .translationY(y)
            .setDuration(3000) // Adjust duration as needed to match the camera animation duration
            .start()
    }

}

