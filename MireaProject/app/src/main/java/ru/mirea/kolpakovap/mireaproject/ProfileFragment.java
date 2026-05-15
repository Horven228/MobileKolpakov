package ru.mirea.kolpakovap.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    // Объявляем переменные для элементов интерфейса
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextHobby;
    private SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Привязываем макет фрагмента
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 2. Инициализируем (находим) поля ввода и кнопку по их ID из XML
        editTextName = view.findViewById(R.id.editTextName);
        editTextAge = view.findViewById(R.id.editTextAge);
        editTextHobby = view.findViewById(R.id.editTextHobby);
        Button buttonSave = view.findViewById(R.id.buttonSaveProfile);

        // 3. Инициализируем хранилище SharedPreferences
        // "MireaProjectPrefs" — имя файла настроек. MODE_PRIVATE — доступ только для этого приложения.
        sharedPref = requireActivity().getSharedPreferences("MireaProjectPrefs", Context.MODE_PRIVATE);

        // 4. АВТОЗАГРУЗКА ДАННЫХ
        // При каждом открытии фрагмента пытаемся достать значения по ключам (NAME, AGE, HOBBY).
        // Если данных нет, вернется пустая строка "".
        editTextName.setText(sharedPref.getString("NAME", ""));
        editTextAge.setText(sharedPref.getString("AGE", ""));
        editTextHobby.setText(sharedPref.getString("HOBBY", ""));

        // 5. СОХРАНЕНИЕ
        // Устанавливаем слушатель на кнопку: при нажатии вызывается метод сохранения
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return view;
    }

    /**
     * Метод для записи данных из полей ввода в постоянную память (SharedPreferences).
     */
    private void saveData() {
        // Для записи данных нужно создать объект Editor (Редактор)
        SharedPreferences.Editor editor = sharedPref.edit();

        // Помещаем данные в формате "Ключ — Значение"
        // getText().toString() берет актуальный текст, который юзер набрал в поле
        editor.putString("NAME", editTextName.getText().toString());
        editor.putString("AGE", editTextAge.getText().toString());
        editor.putString("HOBBY", editTextHobby.getText().toString());

        // Метод apply() сохраняет данные асинхронно (в фоновом потоке),
        // что не дает приложению зависнуть.
        editor.apply();

        // Сообщаем пользователю об успехе
        Toast.makeText(getContext(), "Данные профиля успешно сохранены", Toast.LENGTH_SHORT).show();
    }
}