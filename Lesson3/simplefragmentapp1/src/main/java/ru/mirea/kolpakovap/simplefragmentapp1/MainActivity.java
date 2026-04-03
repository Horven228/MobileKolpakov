package ru.mirea.kolpakovap.simplefragmentapp1;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    Fragment fragment1, fragment2;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем объекты фрагментов
        fragment1 = new FirstFragment();
        fragment2 = new SecondFragment();
        fragmentManager = getSupportFragmentManager();
    }

    // Метод обработки нажатия на кнопки (прописан в XML через android:onClick)
    public void onClick(View view) {
        // Мы ищем контейнер. Если он есть (вертикальный режим), то меняем фрагменты.
        // Если его нет (горизонтальный режим), ничего не делаем, так как там фрагменты уже на экране.
        if (findViewById(R.id.fragmentContainer) != null) {
            int id = view.getId();
            if (id == R.id.btnFragment1) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment1).commit();
            } else if (id == R.id.btnFragment2) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment2).commit();
            }
        }
    }
}