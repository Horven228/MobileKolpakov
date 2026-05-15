package ru.mirea.kolpakovap.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Имя файла, который будет создан в защищенной папке приложения
    private static final String FILE_NAME = "history.txt";
    private EditText editTextDate;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Привязка элементов интерфейса
        editTextDate = findViewById(R.id.editTextDate);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonSave = findViewById(R.id.buttonSave);

        // 1. СЛУШАТЕЛЬ КНОПКИ: Получаем текст из поля ввода и вызываем метод сохранения
        buttonSave.setOnClickListener(v -> {
            String text = editTextDate.getText().toString();
            saveText(text);
        });

        // 2. ЧТЕНИЕ ПРИ ЗАПУСКЕ: Пытаемся прочитать файл сразу, как только открыли экран
        loadTextInBackground();
    }

    // МЕТОД СОХРАНЕНИЯ: Записывает строку в файл.
    private void saveText(String text) {
        // openFileOutput создает поток записи.
        // MODE_PRIVATE означает, что файл будет перезаписан (или создан),
        // и доступ к нему будет только у нашего приложения.
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            // Превращаем строку в массив байтов и пишем в файл
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();

            // После успешной записи обновляем текст на экране
            loadTextInBackground();
        } catch (IOException e) {
            // Если возникла ошибка записи (например, нет места), выводим в лог
            e.printStackTrace();
        }
    }

    // МЕТОД ФОНОВОГО ЧТЕНИЯ:
    private void loadTextInBackground() {
        // Запускаем новый поток (Thread)
        new Thread(() -> {
            try {
                // Искусственная задержка 1 секунда (как в примере методички)
                Thread.sleep(1000);

                // Вызываем метод чтения и получаем строку
                String result = getTextFromFile();

                // Метод .post() позволяет нам отправить приказ главному потоку:
                textViewResult.post(() -> textViewResult.setText(result));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start(); // Запуск потока
    }

    // МЕТОД РАБОТЫ С ПОТОКОМ ВВОДА: Непосредственное чтение байтов из файла.
    public String getTextFromFile() {
        FileInputStream fin = null;
        try {
            // Открываем файл для чтения
            fin = openFileInput(FILE_NAME);

            // Узнаем размер файла и создаем массив байтов нужной длины
            byte[] bytes = new byte[fin.available()];

            // Читаем данные из файла в массив
            fin.read(bytes);

            // Превращаем байты обратно в понятную строку и возвращаем её
            return new String(bytes);
        } catch (IOException ex) {
            // Если файла еще не существует (первый запуск), вернем сообщение
            return "Файл пока пуст";
        } finally {
            // Блок finally выполняется всегда. Обязательно закрываем поток чтения,
            // чтобы освободить оперативную память устройства.
            try {
                if (fin != null) fin.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}