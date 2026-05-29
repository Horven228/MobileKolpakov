package ru.mirea.kolpakovap.mireaproject;

// Аннотация SerializedName используется для связывания имени JSON-поля с Java-полем
// Позволяет называть Java-поля по-своему, не зависимо от названий в JSON-ответе сервера
import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    // Широта (координата местоположения)

    @SerializedName("latitude")
    public double latitude;

    // Долгота (координата местоположения)
    @SerializedName("longitude")
    public double longitude;

    // Содержит температуру, ветер, время и код погоды
    @SerializedName("current_weather")
    public CurrentWeather currentWeather;

    // Вложенный класс для представления объекта "current_weather" в JSON
    // Static класс позволяет существовать независимо от внешнего класса
    // Это стандартный подход для вложенных моделей данных в Retrofit

    public static class CurrentWeather {

        // Температура в градусах Цельсия (°C)
        @SerializedName("temperature")
        public double temperature;

        // Скорость ветра в км/ч
        @SerializedName("windspeed")
        public double windSpeed;

        // Направление ветра в градусах (0° - север, 90° - восток и т.д.)

        @SerializedName("winddirection")
        public int windDirection;

        // Код погоды по стандарту WMO (Всемирная метеорологическая организация)
        @SerializedName("weathercode")
        public int weatherCode;

        // Время замера погоды в формате ISO 8601
        @SerializedName("time")
        public String time;
    }
}