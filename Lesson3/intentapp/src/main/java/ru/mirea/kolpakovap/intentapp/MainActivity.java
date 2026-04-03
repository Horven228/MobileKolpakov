package ru.mirea.kolpakovap.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        // Находим кнопку в макете (убедись, что в activity_main.xml есть кнопка с id "button")
        Button sendButton = findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Код для получения времени
                long dateInMillis = System.currentTimeMillis();
                String format = "yyyy-MM-dd HH:mm:ss";
                final SimpleDateFormat sdf = new SimpleDateFormat(format);
                String dateString = sdf.format(new Date(dateInMillis));

                // Создаем намерение для перехода на SecondActivity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                // Кладем время в "посылку" под ключом "date"
                intent.putExtra("date", dateString);
                startActivity(intent);
            }
        });
    }
}