package ru.mirea.kolpakovap.lesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextGroup;
    private EditText editTextNumber;
    private EditText editTextMovie;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Настройка отступов для безрамочного экрана
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Привязываем переменные к элементам на экране
        editTextGroup = findViewById(R.id.editTextGroup);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextMovie = findViewById(R.id.editTextMovie);
        Button buttonSave = findViewById(R.id.buttonSave);

        // 2. Инициализируем SharedPreferences (название файла "mirea_settings")
        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);

        // 3. ЗАГРУЗКА: При запуске читаем данные из памяти
        String savedGroup = sharedPref.getString("GROUP", "");
        String savedNumber = sharedPref.getString("NUMBER", "");
        String savedMovie = sharedPref.getString("MOVIE", "");

        // Устанавливаем загруженные данные в поля ввода
        editTextGroup.setText(savedGroup);
        editTextNumber.setText(savedNumber);
        editTextMovie.setText(savedMovie);

        // 4. СОХРАНЕНИЕ: Обработка нажатия на кнопку
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем редактор для изменения настроек
                SharedPreferences.Editor editor = sharedPref.edit();

                // Записываем данные из полей ввода в SharedPreferences
                editor.putString("GROUP", editTextGroup.getText().toString());
                editor.putString("NUMBER", editTextNumber.getText().toString());
                editor.putString("MOVIE", editTextMovie.getText().toString());

                // Сохраняем (apply работает асинхронно и быстрее чем commit)
                editor.apply();

                // Выводим уведомление для пользователя
                Toast.makeText(MainActivity.this, "Данные успешно сохранены!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}