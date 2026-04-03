package ru.mirea.kolpakovap.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Получаем данные о книге разработчика из Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView devBookView = findViewById(R.id.textViewDevBook);
            String bookName = extras.getString(MainActivity.KEY);
            devBookView.setText(String.format("Любимая книга разработчика — %s", bookName));
        }

        // 2. Находим EditText и кнопку для возврата результата
        final EditText editText = findViewById(R.id.editTextUserBook);
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userBookName = editText.getText().toString();

                // Создаем Intent для возврата данных
                Intent data = new Intent();
                data.putExtra(MainActivity.USER_MESSAGE, userBookName);

                // Устанавливаем результат OK и прикрепляем данные
                setResult(Activity.RESULT_OK, data);

                // Закрываем Activity (возвращаемся на первый экран)
                finish();
            }
        });
    }
}