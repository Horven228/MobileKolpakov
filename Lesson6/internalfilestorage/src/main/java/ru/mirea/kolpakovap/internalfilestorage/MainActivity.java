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

    private static final String FILE_NAME = "history.txt";
    private EditText editTextDate;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDate = findViewById(R.id.editTextDate);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonSave = findViewById(R.id.buttonSave);

        // 1. Сохранение данных
        buttonSave.setOnClickListener(v -> {
            String text = editTextDate.getText().toString();
            saveText(text);
        });

        // 2. Чтение данных (делаем в новом потоке сразу при запуске или после сохранения)
        loadTextInBackground();
    }

    private void saveText(String text) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
            loadTextInBackground(); // Обновляем экран после сохранения
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTextInBackground() {
        // Создаем новый поток для чтения файла (как в методичке)
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Имитируем задержку (необязательно)
                String result = getTextFromFile();

                // Возвращаемся в основной поток, чтобы обновить UI
                textViewResult.post(() -> textViewResult.setText(result));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public String getTextFromFile() {
        FileInputStream fin = null;
        try {
            fin = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            return new String(bytes);
        } catch (IOException ex) {
            return "Файл пока пуст";
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}