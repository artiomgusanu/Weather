package ip.santarem.weather

data class Weather(
    val cityName: String,             // Nome da cidade
    val temperature: Double,          // Temperatura em graus Celsius
    val weatherDescription: String,   // Descrição do clima
    val iconUrl: String,              // URL do ícone do clima
    val selectedDate: String          // Data escolhida pelo utilizador
)