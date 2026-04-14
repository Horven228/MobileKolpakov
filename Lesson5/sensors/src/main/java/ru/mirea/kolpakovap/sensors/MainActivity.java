package ru.mirea.kolpakovap.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Обработка отступов для безрамочного экрана (сохранено из вашего шаблона)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Находим ListView в разметке
        ListView listView = findViewById(R.id.list_view);

        // 2. Получаем доступ к менеджеру датчиков
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 3. Получаем список всех доступных на устройстве датчиков
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // 4. Создаем массив данных для отображения (Имя датчика и его максимальный диапазон)
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();

        for (int i = 0; i < sensors.size(); i++) {
            HashMap<String, Object> sensorMap = new HashMap<>();
            sensorMap.put("Name", sensors.get(i).getName());
            sensorMap.put("Value", "Макс. диапазон: " + sensors.get(i).getMaximumRange());
            arrayList.add(sensorMap);
        }

        // 5. Настраиваем адаптер для отображения двух строк (имя и значение)
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                arrayList,
                android.R.layout.simple_list_item_2, // Стандартный шаблон Android с двумя текстовыми строками
                new String[]{"Name", "Value"},       // Ключи из HashMap
                new int[]{android.R.id.text1, android.R.id.text2} // ID полей в стандартном шаблоне
        );

        // 6. Привязываем адаптер к списку
        listView.setAdapter(adapter);
    }
}