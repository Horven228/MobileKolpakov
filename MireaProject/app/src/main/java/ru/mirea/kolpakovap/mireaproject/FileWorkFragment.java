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
        // 1. Инфлейтим разметку фрагмента
        View view = inflater.inflate(R.layout.fragment_file_work, container, false);

        // 2. Находим кнопку FAB
        FloatingActionButton fab = view.findViewById(R.id.fabAddFile);

        // 3. Обработка нажатия на кнопку
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        return view;
    }

    // Метод для показа диалогового окна ввода текста
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Создать новую заметку");

        // Создаем поле ввода внутри диалога
        final EditText input = new EditText(getContext());
        input.setHint("Введите текст здесь");
        builder.setView(input);

        // Кнопка сохранения
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userText = input.getText().toString();
                if (!userText.isEmpty()) {
                    saveTextToFile(userText);
                } else {
                    Toast.makeText(getContext(), "Текст пуст", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Кнопка отмены
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Метод для шифрования и сохранения в файл
    private void saveTextToFile(String text) {
        // ШИФРОВАНИЕ: Сдвигаем каждый символ на 1 вперед (Шифр Цезаря)
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] + 1);
        }
        String encryptedText = new String(chars);

        // СОХРАНЕНИЕ во внутреннее хранилище (Internal Storage)
        String fileName = "mirea_note.txt";
        try (FileOutputStream fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(encryptedText.getBytes());
            Toast.makeText(getContext(), "Файл зашифрован и сохранен", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
        }
    }
}