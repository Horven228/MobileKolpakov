package ru.mirea.kolpakovap.employeedb;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        // 1. Сборка базы данных.
        // "database-name" — это имя физического файла базы на диске телефона.
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name")
                // allowMainThreadQueries() позволяет делать запросы в главном потоке.
                .allowMainThreadQueries()
                .build();

        // Получаем объект DAO через нашу базу
        HeroDao heroDao = db.heroDao();

        // 2. Создаем объект Героя и сохраняем его в базу
        Hero hero = new Hero();
        hero.name = "Человек-паук";
        hero.superpower = "Лазание по стенам, чутье";
        heroDao.insert(hero); // Запись улетает в БД

        // 3. Получаем из базы список ВООБЩЕ ВСЕХ героев, которые там накопились
        List<Hero> heroes = heroDao.getAll();

        // 4. Формируем красивую строку для отображения на экране
        StringBuilder sb = new StringBuilder();
        for (Hero h : heroes) {
            String info = "ID: " + h.id + ", Name: " + h.name + ", Power: " + h.superpower;
            // Пишем в Logcat для отладки
            Log.d("RoomDB", info);
            // Добавляем в StringBuilder для экрана
            sb.append(info).append("\n");
        }

        // Выводим накопленный текст в TextView
        textView.setText(sb.toString());
    }
}