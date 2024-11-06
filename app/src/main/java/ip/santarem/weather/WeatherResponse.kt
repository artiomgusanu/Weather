package ip.santarem.weather

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<WeatherInfo>,
    val dt: Long
)

data class Main(
    val temp: Double
)

data class WeatherInfo(
    val description: String,
    val icon: String
)
