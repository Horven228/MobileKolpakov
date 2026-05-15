package ru.mirea.kolpakovap.employeedb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
// Главная "точка входа", которая объединяет таблицы и методы доступа.
@Database(entities = {Hero.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HeroDao heroDao();
}