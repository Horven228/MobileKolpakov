package ru.mirea.kolpakovap.intentfilter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // Метод для открытия браузера (Неявное намерение ACTION_VIEW)
    public void onOpenBrowser(View view) {
        // Создаем Uri адрес сайта
        Uri address = Uri.parse("https://www.mirea.ru/");
        // Создаем намерение: действие "просмотр" для этого адреса
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
        // Запускаем активность (Android предложит браузер)
        startActivity(openLinkIntent);
    }

    //Метод "Поделиться" (Неявное намерение ACTION_SEND)
    public void onShareText(View view) {
        // Создаем намерение на отправку данных
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        // Указываем тип данных - текст
        shareIntent.setType("text/plain");
        // Кладем заголовок и само сообщение (твои ФИО)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MIREA");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Колпаков Александр Павлович, РТУ МИРЭА");
        // Запускаем выбор приложений (диалог Chooser)
        startActivity(Intent.createChooser(shareIntent, "МОИ ФИО"));
    }
}