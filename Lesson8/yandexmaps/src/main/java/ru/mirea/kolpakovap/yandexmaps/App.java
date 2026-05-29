package ru.mirea.kolpakovap.yandexmaps;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // ВСТАВЬ СВОЙ КЛЮЧ НИЖЕ
        MapKitFactory.setApiKey("4fa4afc8-ab87-4457-a5c8-8ddd1b150c5b");
    }
}