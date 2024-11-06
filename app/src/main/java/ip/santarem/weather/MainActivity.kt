package ip.santarem.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var weatherService: WeatherService
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private val weatherList = mutableListOf<Weather>() // Lista para armazenar os dados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configura o layout para acomodar as barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuração do Retrofit e WeatherService
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherService = retrofit.create(WeatherService::class.java)

        // Configura o RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        weatherAdapter = WeatherAdapter(weatherList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = weatherAdapter

        // Lista de Cidades
        val cities = listOf("Lisbon", "New York", "Tokyo", "Berlin", "Madrid")

        // Fetch weather data for each city
        cities.forEach { city ->
            fetchWeatherData(city)
        }
    }

    private fun fetchWeatherData(cityName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherService.getWeatherData(cityName, "34a4fd3405630de47716daa5829b2520").execute()
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val weather = Weather(
                            cityName = it.name,
                            temperature = it.main.temp,
                            weatherDescription = it.weather[0].description,
                            iconUrl = "https://openweathermap.org/img/wn/${it.weather[0].icon}.png", // URL do ícone
                            selectedDate = "Data: ${it.dt}" // Você pode formatar a data conforme necessário
                        )

                        // Adiciona o clima à lista e atualiza o adapter
                        withContext(Dispatchers.Main) {
                            weatherList.add(weather)
                            weatherAdapter.notifyDataSetChanged() // Notifica o adapter que os dados mudaram
                        }
                    }
                } else {
                    Log.e("API_ERROR", "Erro na resposta: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("NETWORK_ERROR", "Erro na chamada de rede", e)
            }
        }
    }
}
