package ru.mirea.kolpakovap.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import ru.mirea.kolpakovap.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0; // Счетчик для логов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Работа с главным потоком
        Thread mainThread = Thread.currentThread();
        binding.textViewInfo.setText("Имя текущего потока: " + mainThread.getName());


        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-09-23, НОМЕР ПО СПИСКУ: 11, МОЙ ЛЮБИМЫЙ ФИЛЬМ: Трансформеры");
        binding.textViewInfo.append("\n Новое имя потока: " + mainThread.getName());

        // Вывод стека в логи
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));
        Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());



        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем новый поток
                new Thread(new Runnable() {
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", "Запущен поток № " + numberThread);

                        try {
                            // Имитация долгой работы (имитируем вычисления)
                            float pairs = Float.parseFloat(binding.editTextPairs.getText().toString());
                            float days = Float.parseFloat(binding.editTextDays.getText().toString());
                            float result = pairs / days;

                            // Возврат в UI-поток для вывода результата
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.textViewResult.setText(String.format("Среднее количество пар: %.2f", result));
                                }
                            });

                        } catch (Exception e) {
                            Log.e("ThreadProject", "Ошибка в вычислениях");
                        }

                        Log.d("ThreadProject", "Выполнен поток № " + numberThread);
                    }
                }).start();
            }
        });
    }
}