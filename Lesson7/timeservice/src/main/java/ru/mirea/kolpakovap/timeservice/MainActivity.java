package ru.mirea.kolpakovap.timeservice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ru.mirea.kolpakovap.timeservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String TAG = "SocketActivity";
    private final String host = "time.nist.gov";
    private final int port = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Обработка системных отступов (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Слушатель кнопки
        binding.buttonGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetTimeTask().execute();
            }
        });
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                // Создание сокета и подключение
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);

                // Пропускаем возможную пустую строку и читаем данные
                reader.readLine();
                timeResult = reader.readLine();

                Log.d(TAG, "Ответ сервера: " + timeResult);
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Ошибка сокета: " + e.getMessage());
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                // ПАРСИНГ СТРОКИ ПО ЗАДАНИЮ
                // Формат: 60456 24-05-20 15:30:45 50 0 0 450.1 UTC(NIST) *
                String[] parts = result.split(" ");
                if (parts.length > 2) {
                    String date = parts[1]; // Дата
                    String time = parts[2]; // Время
                    binding.textViewTime.setText("Дата: " + date + "\nВремя: " + time);
                } else {
                    binding.textViewTime.setText("Неверный формат данных: " + result);
                }
            } else {
                binding.textViewTime.setText("Ошибка получения данных");
            }
        }
    }
}