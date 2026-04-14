package ru.mirea.kolpakovap.workmanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import ru.mirea.kolpakovap.workmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStartWork.setOnClickListener(v -> {
            // 1. Создаем ограничения (Constraints)
            Constraints constraints = new Constraints.Builder()
                    // Требуем наличие любого интернет-соединения
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    // Требуем, чтобы устройство было подключено к источнику питания
                    .setRequiresCharging(true)
                    .build();

            // 2. Создаем запрос на однократное выполнение (OneTimeWorkRequest)
            WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                    .setConstraints(constraints) // Применяем наши ограничения
                    .build();

            // 3. Отправляем задачу в очередь WorkManager
            WorkManager
                    .getInstance(this)
                    .enqueue(uploadWorkRequest);
        });
    }
}