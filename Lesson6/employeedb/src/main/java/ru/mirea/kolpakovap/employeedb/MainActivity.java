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

        TextView textView = findViewById(R.id.textView); // Убедись, что в layout есть TextView с таким id

        // 1. Инициализация базы данных
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "database-name")
                .allowMainThreadQueries() // Разрешаем запросы в основном потоке (только для ЛР!)
                .build();

        HeroDao heroDao = db.heroDao();

        // 2. Добавление героя
        Hero hero = new Hero();
        hero.name = "Человек-паук";
        hero.superpower = "Лазание по стенам, чутье";
        heroDao.insert(hero);

        // 3. Получение списка всех героев
        List<Hero> heroes = heroDao.getAll();

        // 4. Вывод в Лог и на экран
        StringBuilder sb = new StringBuilder();
        for (Hero h : heroes) {
            String info = "ID: " + h.id + ", Name: " + h.name + ", Power: " + h.superpower;
            Log.d("RoomDB", info);
            sb.append(info).append("\n");
        }

        textView.setText(sb.toString());
    }
}