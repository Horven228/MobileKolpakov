package ru.mirea.kolpakovap.mireaproject;

// Импорт класса Call из Retrofit - представляет один HTTP-запрос
// Call используется для отправки запроса и получения ответа асинхронно
import retrofit2.Call;
// Аннотация GET - указывает, что это HTTP GET запрос
import retrofit2.http.GET;
// Аннотация Query - указывает, что параметр будет добавлен в URL как query-параметр
import retrofit2.http.Query;

/**
 * ApiService - интерфейс для описания HTTP-запросов к API погоды
 *
 * Retrofit использует этот интерфейс для автоматической генерации кода,
 * который выполняет сетевые запросы и преобразует JSON-ответы в Java-объекты
 *
 * Это пример использования библиотеки Retrofit из Практики №7
 */
public interface ApiService {

    /**
     * @param latitude - широта (Query-параметр "latitude")
     * @param longitude - долгота (Query-параметр "longitude")
     * @param currentWeather - флаг текущей погоды (Query-параметр "current_weather")
     */
    @GET("v1/forecast")
    Call<WeatherResponse> getWeather(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("current_weather") boolean currentWeather
    );
}