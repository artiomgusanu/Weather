package ip.santarem.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class WeatherAdapter(private val weatherList: List<Weather>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityName: TextView = itemView.findViewById(R.id.cityName)
        private val weatherDescription: TextView = itemView.findViewById(R.id.weatherDescription)
        private val temperature: TextView = itemView.findViewById(R.id.temperature)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)

        fun bind(weather: Weather) {
            cityName.text = weather.cityName
            weatherDescription.text = weather.weatherDescription
            temperature.text = "${weather.temperature} °C"

            // Carrega o ícone do clima usando Glide
            Glide.with(itemView.context)
                .load(weather.iconUrl)
                .into(weatherIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    override fun getItemCount(): Int = weatherList.size
}
