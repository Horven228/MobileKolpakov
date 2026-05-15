package ru.mirea.kolpakovap.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
// описываем, какие действия (SQL-запросы) мы хотим совершать с базой.
// @Dao помечает интерфейс для работы с базой данных.
@Dao
public interface HeroDao {
    // @Query позволяет писать любые SQL запросы вручную.
    // Этот метод вернет список всех героев из таблицы.
    @Query("SELECT * FROM hero")
    List<Hero> getAll();

    // Запрос с параметром. Вместо :id подставится значение из аргумента метода.
    @Query("SELECT * FROM hero WHERE id = :id")
    Hero getById(long id);

    // Специальные аннотации для стандартных действий (не нужно писать SQL)
    @Insert
    void insert(Hero hero); // Добавить запись

    @Update
    void update(Hero hero); // Обновить (изменить) запись

    @Delete
    void delete(Hero hero); // Удалить запись
}