package ru.mirea.kolpakovap.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener; // Слушатель событий датчиков
import android.hardware.SensorManager;      // Менеджер датчиков
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// Класс реализует SensorEventListener, чтобы реагировать на изменения "железа"
public class SensorsFragment extends Fragment implements SensorEventListener {
    private final String TAG = "SensorsFragment"; // Тег для логов
    private SensorManager sensorManager;           // Объект для работы с датчиками
    private Sensor accelerometer;                 // Переменная для акселерометра
    private Sensor magnetometer;                  // Переменная для магнитного датчика

    // Массивы для хранения последних данных с датчиков (3 координаты: X, Y, Z)
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];

    // Флаги: получили ли мы данные от датчиков?
    private boolean isAccelSet = false;
    private boolean isMagSet = false;
    private TextView directionText; // Текстовое поле на экране

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Привязываем XML-разметку к фрагменту
        View root = inflater.inflate(R.layout.fragment_sensors, container, false);
        directionText = root.findViewById(R.id.textViewDirection);

        // Инициализируем сервис управления датчиками
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        // Выбираем в системе конкретные датчики
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Log.d(TAG, "Датчики успешно инициализированы");
        return root;
    }

    // Метод вызывается при каждом движении устройства
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Если событие пришло от акселерометра — сохраняем его значения
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
            isAccelSet = true;
        }
        // Если от магнетометра — сохраняем его значения
        else if (event.sensor == magnetometer) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            isMagSet = true;
        }

        // Если у нас есть данные от обоих датчиков, считаем угол
        if (isAccelSet && isMagSet) {
            float[] r = new float[9];           // Матрица вращения
            float[] orientation = new float[3]; // Результирующие углы

            // Вычисляем матрицу вращения (связываем наклон и магнитный север)
            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                // Превращаем матрицу в понятные углы поворота
                SensorManager.getOrientation(r, orientation);
                // Переводим азимут из радианов в градусы
                float azimuthInDegrees = (float) Math.toDegrees(orientation[0]);
                // Выводим текст: округляем градусы и ставим значок °
                directionText.setText("Направление: " + Math.round(azimuthInDegrees) + "°");

                // Пишем в логи текущий азимут (для контроля практики)
                Log.d(TAG, "Текущий азимут: " + azimuthInDegrees);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Включаем датчики, когда зашли на экран
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        Log.d(TAG, "Слушатели включены");
    }

    @Override
    public void onPause() {
        super.onPause();
        // Выключаем датчики при выходе, чтобы не тратить заряд
        sensorManager.unregisterListener(this);
        Log.d(TAG, "Слушатели выключены (onPause)");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {} // Нужен по правилам интерфейса
}