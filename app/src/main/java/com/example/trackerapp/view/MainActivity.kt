package com.example.trackerapp.view




import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackerapp.databinding.ActivityMainBinding
import com.example.trackerapp.model.LocationData
import com.example.trackerapp.service.LocationForegroundService
import com.example.trackerapp.viewModel.LocationAdapter
import io.realm.Realm


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realm: Realm
    private lateinit var adapter: LocationAdapter
    private lateinit var locationDataList: List<LocationData>

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkLocationPermission()
        realm = Realm.getDefaultInstance()
        val locationList = realm.where(LocationData::class.java).findAll()

        adapter = LocationAdapter(locationList) { locationData ->
            // Handle item click here
            val intent = Intent(this, MapViewActivity::class.java)
            intent.putExtra("latitude", locationData.latitude)
            intent.putExtra("longitude", locationData.longitude)
            intent.putExtra("timestamp",locationData.timestamp)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


    private var locationAccess=123
    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkLocationPermission() {
        if(Build.VERSION.SDK_INT>=23){

            if(ActivityCompat.
                checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    locationAccess)
                return
            }else{
                trackLocation()

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){

            locationAccess->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    trackLocation()
                } else{
                    Toast.makeText(this,"Unable to access your location ",Toast.LENGTH_LONG).show()
                    checkLocationPermission()
                }
            }
        }
        when(requestCode){

            FOREGROUND_SERVICE_PERMISSION_CODE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    startForegroundService(Intent(this, LocationForegroundService::class.java))

                } else{
                    Toast.makeText(this,"Provide Service Permission ",Toast.LENGTH_LONG).show()
                    getServicePermission()
                }

            }


        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun trackLocation() {

        getServicePermission()

    }
    private val FOREGROUND_SERVICE_PERMISSION_CODE = 101

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getServicePermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestPermissions(arrayOf(android.Manifest.permission.FOREGROUND_SERVICE),FOREGROUND_SERVICE_PERMISSION_CODE)

        } else {
            // Permission already granted, start the service
            startForegroundService(Intent(this, LocationForegroundService::class.java))
        }

    }


}