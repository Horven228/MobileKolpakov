package ru.mirea.kolpakovap.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.kolpakovap.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handler главного потока (принимает ответы от MyLooper)
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d("MainActivity", "Результат получен: " + result);
                binding.textViewResult.setText(result);
            }
        };

        // Создаем и запускаем фоновый поток (Looper)
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.buttonExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем "письмо" для фонового потока
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();

                // Собираем данные из интерфейса
                String job = binding.editTextJob.getText().toString();
                int age = Integer.parseInt(binding.editTextAge.getText().toString());

                bundle.putString("JOB", job);
                bundle.putInt("AGE", age);
                msg.setData(bundle);

                // Отправляем сообщение в Handler фонового потока
                if (myLooper.mHandler != null) {
                    myLooper.mHandler.sendMessage(msg);
                }
            }
        });
    }
}