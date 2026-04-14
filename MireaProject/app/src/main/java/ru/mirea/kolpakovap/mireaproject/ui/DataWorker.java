package ru.mirea.kolpakovap.mireaproject.ui;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class DataWorker extends Worker {
    public DataWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("MireaProjectWorker", "Задача началась: Имитация работы...");
        try {
            TimeUnit.SECONDS.sleep(3); // Работаем 3 секунды
        } catch (InterruptedException e) {
            return Result.failure();
        }
        Log.d("MireaProjectWorker", "Задача успешно завершена!");
        return Result.success();
    }
}
