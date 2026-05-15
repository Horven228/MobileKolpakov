package ru.mirea.kolpakovap.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileWorkFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. "Раздуваем" (создаем) визуальный интерфейс фрагмента из XML-файла
        View view = inflater.inflate(R.layout.fragment_file_work, container, false);

        // 2. Находим на макете кнопку FAB (Floating Action Button) по её ID
        FloatingActionButton fab = view.findViewById(R.id.fabAddFile);

        // 3. Устанавливаем слушатель нажатия: при клике на кнопку вызовется метод показа диалога
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        return view;
    }

    // Метод для создания и отображения всплывающего окна (AlertDialog),чтобы пользователь мог ввести текст заметки.
    private void showInputDialog() {
        // Создаем конструктор диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Создать новую заметку");

        // Программно создаем поле ввода (EditText) для этого окна
        final EditText input = new EditText(getContext());
        input.setHint("Введите текст здесь");
        // Устанавливаем созданное поле ввода как центральный элемент диалога
        builder.setView(input);

        // Настраиваем кнопку "Сохранить"
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userText = input.getText().toString();
                // Если текст не пустой — отправляем его на сохранение в файл
                if (!userText.isEmpty()) {
                    saveTextToFile(userText);
                } else {
                    Toast.makeText(getContext(), "Текст пуст", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Настраиваем кнопку "Отмена" (просто закрывает окно)
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        // Показываем готовое окно пользователю
        builder.show();
    }

    /**
     * Метод для обработки текста и его физической записи в память устройства.
     */
    private void saveTextToFile(String text) {
        // --- ШИФРОВАНИЕ (Творческое задание) ---
        // Используем Шифр Цезаря со сдвигом 1: превращаем строку в массив символов
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // Увеличиваем код каждого символа на 1 (например, 'A' станет 'B')
            chars[i] = (char) (chars[i] + 1);
        }
        // Собираем зашифрованную строку обратно
        String encryptedText = new String(chars);

        // --- СОХРАНЕНИЕ В ПРИВАТНУЮ ПАПКУ ПРИЛОЖЕНИЯ ---
        String fileName = "mirea_note.txt"; // Имя нашего файла
        // Используем try-with-resources для автоматического закрытия потока записи
        // openFileOutput сохраняет файл по пути /data/data/ваш.пакет/files/
        try (FileOutputStream fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE)) {
            // Переводим строку в байты и записываем в файл
            fos.write(encryptedText.getBytes());
            Toast.makeText(getContext(), "Файл зашифрован и сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // Если произошла системная ошибка (например, нет места), выводим её в консоль
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
        }
    }
}