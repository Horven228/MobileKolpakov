package ru.mirea.kolpakovap.buttonclicker; // замените на ваш package

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textViewStudent;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStudent = findViewById(R.id.textViewStudent);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        checkBox = findViewById(R.id.checkBox);

        // Первый способ: обработчик через анонимный класс
        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStudent.setText("Мой номер по списку № Х");
                checkBox.setChecked(true); // CheckBox включается
            }
        };
        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);
    }

    // Второй способ: обработчик через атрибут onClick в XML
    public void onItIsNotMeClick(View view) {
        textViewStudent.setText("Это не я сделал");
        checkBox.setChecked(false); // CheckBox выключается
    }
}