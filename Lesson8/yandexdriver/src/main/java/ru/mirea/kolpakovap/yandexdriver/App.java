package ru.mirea.kolpakovap.yandexdriver;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Твой API-ключ
        MapKitFactory.setApiKey("4fa4afc8-ab87-4457-a5c8-8ddd1b150c5b");
    }
}