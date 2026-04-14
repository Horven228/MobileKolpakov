package ru.mirea.kolpakovap.workmanager;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class UploadWorker extends Worker {
    static final String TAG = "UploadWorker";

    // Конструктор обязателен для всех Worker
    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: start");
        try {
            // Имитируем выполнение долгой задачи (например, выгрузка фото)
            // Поток заснет на 5 секунд
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Result.failure(); // Сообщаем, что задача провалена
        }

        Log.d(TAG, "doWork: end");
        return Result.success(); // Сообщаем, что задача успешно выполнена
    }
}