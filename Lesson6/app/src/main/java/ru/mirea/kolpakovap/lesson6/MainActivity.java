// Указывает на принадлежность файла к твоему проекту
package ru.mirea.kolpakovap.lesson6;

// Импорт необходимых классов Android для работы с интерфейсом и настройками
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

    // Объявляем переменные для полей ввода (чтобы получить текст от пользователя)
    private EditText editTextGroup;
    private EditText editTextNumber;
    private EditText editTextMovie;

    // Объявляем переменную для работы с хранилищем настроек
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Код для настройки отступов (Insets).
        // Нужен, чтобы элементы интерфейса не перекрывались строкой состояния (время, батарея)
        // или нижней навигационной панелью телефона.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. СВЯЗЫВАНИЕ: Находим элементы в XML-файле по их ID и привязываем к Java-переменным
        editTextGroup = findViewById(R.id.editTextGroup);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextMovie = findViewById(R.id.editTextMovie);
        Button buttonSave = findViewById(R.id.buttonSave);

        // 2. ИНИЦИАЛИЗАЦИЯ ХРАНИЛИЩА:
        // MODE_PRIVATE — означает, что доступ к файлу будет только у этого приложения (безопасно).
        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);

        // 3. ЗАГРУЗКА ДАННЫХ:
        // Метод getString(ключ, значение_по_умолчанию).
        // Если в памяти еще ничего нет (первый запуск), вернется пустая строка "".
        String savedGroup = sharedPref.getString("GROUP", "");
        String savedNumber = sharedPref.getString("NUMBER", "");
        String savedMovie = sharedPref.getString("MOVIE", "");

        // Сразу отображаем загруженные данные в полях ввода, чтобы пользователь их увидел
        editTextGroup.setText(savedGroup);
        editTextNumber.setText(savedNumber);
        editTextMovie.setText(savedMovie);

        // 4. СОХРАНЕНИЕ ДАННЫХ: Устанавливаем слушатель нажатия на кнопку
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Чтобы записывать данные, нужно создать объект Editor (Редактор)
                SharedPreferences.Editor editor = sharedPref.edit();

                // Кладем данные в "корзину" редактора в формате (КЛЮЧ, ЗНАЧЕНИЕ)
                // Ключи должны быть такими же, как при загрузке!
                editor.putString("GROUP", editTextGroup.getText().toString());
                editor.putString("NUMBER", editTextNumber.getText().toString());
                editor.putString("MOVIE", editTextMovie.getText().toString());

                // ФИНАЛИЗАЦИЯ: Команда применить изменения.
                // Метод apply() сохраняет данные в фоне, не тормозя работу приложения.
                editor.apply();

                // Обратная связь: показываем пользователю, что кнопка сработала
                Toast.makeText(MainActivity.this, "Данные успешно сохранены!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}