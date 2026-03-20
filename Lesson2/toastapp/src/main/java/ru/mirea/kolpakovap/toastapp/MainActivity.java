package ru.mirea.kolpakovap.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Метод для обработки нажатия на кнопку
    public void onClickCount(View view) {
        EditText editText = findViewById(R.id.editTextCharacters);
        // Считаем количество символов в поле ввода
        int count = editText.getText().toString().length();

        // Формируем сообщение согласно методичке (замените Х на свои реальные данные)
        String message = "СТУДЕНТ № 12 ГРУППА БСБО-09-23 Количество символов - " + count;

        // Выводим Toast
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}