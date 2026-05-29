package ru.mirea.kolpakovap.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkFragment extends Fragment {

    // UI компоненты
    private TextView temperatureTextView;   // Температура
    private TextView weatherDescTextView;  // Описание погоды (Ясно, Дождь и т.д.)
    private TextView windTextView;         // Скорость ветра
    private TextView timeTextView;         // Время обновления
    private Button loadButton;              // Кнопка загрузки

    // Координаты Москвы
    // Широта: 55.7558, Долгота: 37.6176
    private final double MOSCOW_LAT = 55.7558;
    private final double MOSCOW_LON = 37.6176;

    // onCreateView - создание UI фрагмента

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Преобразуем fragment_network.xml в объект View
        View view = inflater.inflate(R.layout.fragment_network, container, false);

        // Находим все View по их ID из layout-файла
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        weatherDescTextView = view.findViewById(R.id.weatherDescTextView);
        windTextView = view.findViewById(R.id.windTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        loadButton = view.findViewById(R.id.loadFactButton);

        // ============================================================
        // НАСТРОЙКА RETROFIT
        // ============================================================
        // Retrofit.Builder - строитель для конфигурации HTTP-клиента
        Retrofit retrofit = new Retrofit.Builder()
                // Базовый URL (корневой адрес API)
                // Все запросы будут начинаться с этого URL
                .baseUrl("https://api.open-meteo.com/")

                // Добавляем конвертер Gson для автоматического парсинга JSON
                // Gson превращает JSON строку в Java-объект WeatherResponse
                .addConverterFactory(GsonConverterFactory.create())

                // Строим объект Retrofit
                .build();

        // Создаём реализацию интерфейса ApiService
        // Retrofit генерирует код, который выполняет запросы
        ApiService apiService = retrofit.create(ApiService.class);


        // ОБРАБОТЧИК КНОПКИ
        // Устанавливаем слушатель нажатия на кнопку
        // Погода загружается ТОЛЬКО при нажатии (не автоматически)
        loadButton.setOnClickListener(v -> loadWeather(apiService));

        return view;
    }

    // Загрузка погоды через Retrofit

    private void loadWeather(ApiService apiService) {
        // Обновляем UI перед запросом
        weatherDescTextView.setText("Загрузка погоды...");
        temperatureTextView.setText("--°C");
        windTextView.setText("");
        timeTextView.setText("");

        // Блокируем кнопку, чтобы не отправлять несколько запросов одновременно
        loadButton.setEnabled(false);
        loadButton.setText("Загрузка...");

        // ============================================================
        // АСИНХРОННЫЙ ЗАПРОС
        // ============================================================
        // enqueue - выполняет запрос в фоновом потоке (не блокирует UI)
        // Callback - интерфейс с методами onResponse (успех) и onFailure (ошибка)
        apiService.getWeather(MOSCOW_LAT, MOSCOW_LON, true)
                .enqueue(new Callback<WeatherResponse>() {

                    // Вызывается при успешном получении ответа от сервера
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        // Разблокируем кнопку
                        loadButton.setEnabled(true);
                        loadButton.setText("Узнать погоду");

                        // Проверяем успешность ответа (код 200 OK)
                        if (response.isSuccessful() && response.body() != null) {
                            // Получаем объект с погодой
                            WeatherResponse weather = response.body();

                            // Проверяем, что вложенный объект currentWeather существует
                            if (weather.currentWeather != null) {
                                // Извлекаем данные
                                double temp = weather.currentWeather.temperature;
                                double windSpeed = weather.currentWeather.windSpeed;
                                String time = weather.currentWeather.time;

                                // Обновляем UI (округление температуры до целого)
                                temperatureTextView.setText(Math.round(temp) + "°C");
                                windTextView.setText("💨 Ветер: " + windSpeed + " км/ч");
                                timeTextView.setText("🕐 Обновлено: " + time);

                                // Преобразуем числовой код погоды в текстовое описание с эмодзи
                                String description = getWeatherDescription(weather.currentWeather.weatherCode);
                                weatherDescTextView.setText(description);
                            }
                        } else {
                            // Сервер вернул ошибку (например, 404 Not Found)
                            weatherDescTextView.setText("Ошибка загрузки погоды");
                        }
                    }

                    // Вызывается при ошибке сети (нет интернета, таймаут и т.д.)
                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        // Разблокируем кнопку
                        loadButton.setEnabled(true);
                        loadButton.setText("Узнать погоду");

                        // Показываем сообщение об ошибке
                        weatherDescTextView.setText("Ошибка сети: " + t.getMessage());
                        temperatureTextView.setText("--°C");
                    }
                });
    }

    // Преобразование числового кода погоды в текстовое описание

    private String getWeatherDescription(int weatherCode) {
        switch (weatherCode) {
            case 0:
                return "☀️ Ясно";
            case 1: case 2: case 3:
                return "⛅ Переменная облачность";
            case 45: case 48:
                return "🌫️ Туман";
            case 51: case 53: case 55:
                return "🌧️ Морось";
            case 61: case 63: case 65:
                return "🌧️ Дождь";
            case 71: case 73: case 75:
                return "❄️ Снег";
            case 80: case 81: case 82:
                return "🌧️ Ливень";
            case 95:
                return "⛈️ Гроза";
            default:
                return "🌡️ Другое";
        }
    }
}