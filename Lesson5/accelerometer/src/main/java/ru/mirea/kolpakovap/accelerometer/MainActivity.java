package ru.mirea.kolpakovap.accelerometer; // проверьте свой package

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Добавляем implements SensorEventListener
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView azimuthTextView;
    private TextView pitchTextView;
    private TextView rollTextView;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем элементы UI
        azimuthTextView = findViewById(R.id.textViewAzimuth);
        pitchTextView = findViewById(R.id.textViewPitch);
        rollTextView = findViewById(R.id.textViewRoll);

        // Инициализируем SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Инициализируем сам датчик акселерометра
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    // Включаем датчик, когда приложение на экране
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Выключаем датчик, когда приложение свернуто, чтобы не тратить батарею
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // Главный метод: вызывается каждый раз при изменении положения телефона
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float valueX = event.values[0]; // Ось X
            float valueY = event.values[1]; // Ось Y
            float valueZ = event.values[2]; // Ось Z

            // Обновляем текст в полях
            azimuthTextView.setText("X (Azimuth): " + valueX);
            pitchTextView.setText("Y (Pitch): " + valueY);
            rollTextView.setText("Z (Roll): " + valueZ);
        }
    }

    // Этот метод должен быть по интерфейсу, но он нам не нужен (точность)
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}