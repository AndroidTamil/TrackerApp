package com.example.trackerapp
import android.app.Application
import android.content.Intent
import com.example.trackerapp.service.LocationForegroundService
import io.realm.FieldAttribute
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import java.util.Date

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        // Define your migration strategy
        val migration = RealmMigration { realm, oldVersion, newVersion ->
            // Perform migration logic here
            if (oldVersion == 0L) {
                val schema = realm.schema
                val locationDataSchema = schema.get("LocationData")
                // Check if LocationData exists and if the 'timestamp' field is missing
                if (locationDataSchema != null && !locationDataSchema.hasField("timestamp")) {
                    // Add the 'timestamp' field with a default value
                    locationDataSchema.addField("timestamp", Date::class.java, FieldAttribute.REQUIRED)
                }
            }
        }

        // Configure Realm with migration
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .name("Track")
            .schemaVersion(1) // Increment the schema version
            .migration(migration) // Apply migration strategy
            .build()

        // Set the configuration, this will automatically handle schema versioning
        Realm.setDefaultConfiguration(config)

        startService(Intent(this, LocationForegroundService::class.java))
    }
}
