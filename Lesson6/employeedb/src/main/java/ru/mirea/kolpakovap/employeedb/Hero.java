package ru.mirea.kolpakovap.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
// Этот класс описывает, как будет выглядеть таблица в базе данных.
@Entity(tableName = "hero") // Указываем имя таблицы
public class Hero {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String superpower;
}