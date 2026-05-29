package ru.mirea.kolpakovap.mireaproject;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    // Вставь свой ключ Яндекса
    private final String MAPKIT_API_KEY = "898c0481-d91f-47bd-884f-11a6fe58283b";

    @Override
    public void onCreate() {
        super.onCreate();
        // Устанавливаем ключ ДО вызова инициализации MapKitFactory
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}