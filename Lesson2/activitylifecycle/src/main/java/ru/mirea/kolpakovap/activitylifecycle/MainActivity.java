package ru.mirea.kolpakovap.activitylifecycle;

import android.os.Bundle;
import android.util.Log; // 1. Обязательно добавь этот импорт

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // 2. Объявляем переменную TAG для фильтрации логов
    private String TAG = "Lifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Лог для метода onCreate
        Log.i(TAG, "Метод onCreate() вызван");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 3. Дописываем все остальные методы жизненного цикла:

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Метод onStart() вызван");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Метод onResume() вызван");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Метод onPause() вызван");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Метод onStop() вызван");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Метод onDestroy() вызван");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Метод onRestart() вызван");
    }
}