package ru.mirea.kolpakovap.systemintentsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 1. Метод для звонка
    public void onClickCall(View view) {
        // Создаем намерение с действием ACTION_DIAL (открыть клавиатуру телефона)
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // Указываем данные: протокол "tel:" и сам номер
        intent.setData(Uri.parse("tel:89811112233"));
        // Отправляем запрос системе Android
        startActivity(intent);
    }

    // 2. Метод для браузера
    public void onClickOpenBrowser(View view) {
        // Действие ACTION_VIEW — универсальное действие "Посмотреть"
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://developer.android.com"));
        startActivity(intent);
    }

    // 3. Метод для карт
    public void onClickOpenMaps(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:55.749479,37.613944"));
        startActivity(intent);
    }
}