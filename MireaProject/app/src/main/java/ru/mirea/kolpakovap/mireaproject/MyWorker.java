package ru.mirea.kolpakovap.mireaproject;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {
    static final String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: ФОНОВАЯ ЗАДАЧА НАЧАЛАСЬ!");
        try {
            TimeUnit.SECONDS.sleep(10); // Ждем 10 секунд
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doWork: ФОНОВАЯ ЗАДАЧА УСПЕШНО ЗАВЕРШЕНА!");
        return Result.success();
    }
}
