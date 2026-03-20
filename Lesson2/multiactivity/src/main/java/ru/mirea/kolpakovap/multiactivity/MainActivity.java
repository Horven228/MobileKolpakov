package ru.mirea.kolpakovap.multiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Метод для кнопки
    public void onClickNewActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);

        intent.putExtra("key", "МИРЭА - Колпаков А.П. Группа БСБО-09-23");
        startActivity(intent);
    }
}