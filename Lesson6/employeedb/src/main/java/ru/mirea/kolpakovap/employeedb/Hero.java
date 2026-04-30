package ru.mirea.kolpakovap.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hero") // Указываем имя таблицы
public class Hero {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String superpower;
}