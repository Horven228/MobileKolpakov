package ru.mirea.kolpakovap.notebook;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFileName;
    private EditText editTextQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextQuote = findViewById(R.id.editTextQuote);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonLoad = findViewById(R.id.buttonLoad);

        buttonSave.setOnClickListener(v -> saveFileToExternalStorage());
        buttonLoad.setOnClickListener(v -> readFileFromExternalStorage());
    }

    // Проверка доступности памяти
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private void saveFileToExternalStorage() {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "Внешняя память недоступна", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = editTextFileName.getText().toString();
        String content = editTextQuote.getText().toString();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            osw.write(content);
            Toast.makeText(this, "Файл сохранен в Documents", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Notebook", "Ошибка записи", e);
        }
    }

    private void readFileFromExternalStorage() {
        String fileName = editTextFileName.getText().toString();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        if (!file.exists()) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            editTextQuote.setText(sb.toString());
            Toast.makeText(this, "Данные загружены", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Notebook", "Ошибка чтения", e);
        }
    }
}