package ru.mirea.kolpakovap.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// Библиотеки для работы с зашифрованными настройками
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Настройка отступов для корректного отображения под системными панелями
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Находим текстовое поле, куда будем выводить расшифрованное имя
        TextView textViewPoetName = findViewById(R.id.textViewPoetName);

        try {
            // 1. ГЕНЕРАЦИЯ МАСТЕР-КЛЮЧА
            // Этот ключ создается в аппаратном хранилище AndroidKeyStore.
            // Он будет использоваться для шифрования самого файла настроек.
            String mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // 2. ИНИЦИАЛИЗАЦИЯ ЗАШИФРОВАННОГО ХРАНИЛИЩА
            // Параметры AES256_SIV и AES256_GCM — современные стандарты шифрования.
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // 3. СОХРАНЕНИЕ ДАННЫХ
            // В момент вызова apply() библиотека берет строку "Александр Сергеевич Пушкин",
            // шифрует её и записывает в XML файл в виде набора случайных байтов.
            secureSharedPreferences.edit().putString("POET", "Александр Сергеевич Пушкин").apply();

            // 4. ЧТЕНИЕ ДАННЫХ
            // При чтении библиотека автоматически находит нужный ключ, расшифровывает значение
            // и возвращает нам обычный понятный текст.
            String poetName = secureSharedPreferences.getString("POET", "Неизвестно");

            // Устанавливаем полученное имя в TextView
            textViewPoetName.setText(poetName);

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}