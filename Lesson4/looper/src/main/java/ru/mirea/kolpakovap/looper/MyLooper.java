package ru.mirea.kolpakovap.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler; // Handler этого (фонового) потока
    private Handler mainHandler; // Ссылка на Handler главного потока

    public MyLooper(Handler mainThreadHandler) {
        this.mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d("MyLooper", "Рабочий поток запущен");
        Looper.prepare(); // Шаг 1: Подготовка очереди сообщений

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // Шаг 2: Извлекаем данные
                String job = msg.getData().getString("JOB");
                int age = msg.getData().getInt("AGE");

                // Шаг 3: Имитируем задержку (возраст в секундах)
                try {
                    Thread.sleep(age * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Шаг 4: Логика (вычисление длины строки)
                int length = job.length();
                String resultStr = "Работа: " + job + ". Длина: " + length + ". Задержка: " + age + "с.";

                // Шаг 5: Отправляем результат обратно в главный поток
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", resultStr);
                message.setData(bundle);
                mainHandler.sendMessage(message);
            }
        };

        Looper.loop(); // Шаг 3: Запуск бесконечного цикла обработки
    }
}