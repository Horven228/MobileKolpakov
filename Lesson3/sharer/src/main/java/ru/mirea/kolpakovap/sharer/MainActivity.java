package ru.mirea.kolpakovap.sharer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Настройка системных отступов (EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- ЛОГИКА ЗАДАНИЯ 1.6 ---

        // 1. Находим кнопку в activity_main.xml по ID
        Button btnShare = findViewById(R.id.btnShare);

        // 2. Устанавливаем слушатель нажатий
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем намерение для отправки (ACTION_SEND)
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                // Указываем тип данных (*/* — любые данные)
                intent.setType("*/*");

                // Добавляем текст "Mirea" в "экстра" (согласно методичке стр. 10)
                intent.putExtra(Intent.EXTRA_TEXT, "Mirea");

                // Вызываем диалоговое окно выбора приложения с помощью createChooser
                startActivity(Intent.createChooser(intent, "Выбор за вами!"));
            }
        });
    }
}