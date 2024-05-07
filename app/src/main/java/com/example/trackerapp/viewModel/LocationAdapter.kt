package com.example.trackerapp.viewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackerapp.R
import com.example.trackerapp.model.LocationData
class LocationAdapter(
    private val locationList: List<LocationData>,
    private val onItemClick: (LocationData) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val latitudeTextView: TextView = itemView.findViewById(R.id.latitudeTextView)
        private val longitudeTextView: TextView = itemView.findViewById(R.id.longitudeTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(locationData: LocationData) {
            latitudeTextView.text = "Latitude: ${locationData.latitude}"
            longitudeTextView.text = "Longitude: ${locationData.longitude}"
            timestampTextView.text = "Timestamp: ${locationData.timestamp}"

            // Set click listener
            itemView.setOnClickListener { onItemClick(locationData) }
        }
    }
}
