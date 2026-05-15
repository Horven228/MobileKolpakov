package ru.mirea.kolpakovap.audiorecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    // Константа для идентификации запроса разрешений
    private static final int REQUEST_CODE_PERMISSION = 200;
    private final String TAG = "MainActivity";

    // Элементы интерфейса
    private Button recordButton;
    private Button playButton;

    // Объекты для записи и воспроизведения
    private MediaRecorder recorder = null; // Отвечает за захват звука с микрофона
    private MediaPlayer player = null;     // Отвечает за проигрывание файла
    private String recordFilePath = null;  // Путь, по которому будет лежать аудиофайл

    // Флаги состояний (помогают кнопкам работать как переключатели)
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;
    private boolean isWork = false; // Флаг: получены ли все разрешения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Связываем переменные с кнопками в XML
        recordButton = findViewById(R.id.recordButton);
        playButton = findViewById(R.id.playButton);

        // Кнопка воспроизведения заблокирована, пока нет записи
        playButton.setEnabled(false);

        // Генерируем путь к файлу в папку Music внутри хранилища приложения
        recordFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp").getAbsolutePath();

        // --- ПРОВЕРКА РАЗРЕШЕНИЙ ---
        // Проверяем статус доступа к микрофону и внешней памяти
        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED
                && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true; // Если права есть, всё в порядке
        } else {
            // Если прав нет, запрашиваем их у пользователя через системное окно
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        // --- ЛОГИКА КНОПКИ ЗАПИСИ ---
        recordButton.setOnClickListener(v -> {
            if (isStartRecording) {
                // Если мы нажали кнопку в состоянии "Начать запись"
                recordButton.setText("Stop recording");
                playButton.setEnabled(false); // Нельзя играть, пока пишем
                startRecording();
            } else {
                // Если мы нажали кнопку в состоянии "Остановить запись"
                recordButton.setText("Start recording");
                playButton.setEnabled(true);  // Теперь файл можно прослушать
                stopRecording();
            }
            // Меняем флаг на противоположный
            isStartRecording = !isStartRecording;
        });

        // --- ЛОГИКА КНОПКИ ВОСПРОИЗВЕДЕНИЯ ---
        playButton.setOnClickListener(v -> {
            if (isStartPlaying) {
                // Нажали "Начать проигрывание"
                playButton.setText("Stop playing");
                recordButton.setEnabled(false); // Нельзя писать, пока слушаем
                startPlaying();
            } else {
                // Нажали "Остановить проигрывание"
                playButton.setText("Start playing");
                recordButton.setEnabled(true);
                stopPlaying();
            }
            // Меняем флаг на противоположный
            isStartPlaying = !isStartPlaying;
        });
    }

    // Настройка и запуск MediaRecorder (Запись)
    private void startRecording() {
        recorder = new MediaRecorder();
        // 1. Указываем источник звука - микрофон
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 2. Указываем формат файла контейнера - 3GPP
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // 3. Указываем путь к итоговому файлу
        recorder.setOutputFile(recordFilePath);
        // 4. Указываем аудио-кодек для сжатия (AMR_NB подходит для голоса)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare(); // Проверка готовности системы к записи
            recorder.start();   // Начало записи
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    // Остановка записи и освобождение ресурсов
    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();    // Физическая остановка захвата звука
            recorder.release(); // Очистка памяти и освобождение микрофона для других приложений
            recorder = null;
        }
    }

    // Настройка и запуск MediaPlayer (Воспроизведение)
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            // Указываем путь к нашему записанному файлу
            player.setDataSource(recordFilePath);
            player.prepare(); // Декодирование файла и подготовка к запуску
            player.start();   // Начало воспроизведения через динамик
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    // Остановка проигрывания и освобождение ресурсов
    private void stopPlaying() {
        if (player != null) {
            player.release(); // Освобождаем аудио-чипсет устройства
            player = null;
        }
    }

    // Метод вызывается автоматически после того, как пользователь ответил на запрос разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            // Если первый элемент массива (микрофон) подтвержден пользователем
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}