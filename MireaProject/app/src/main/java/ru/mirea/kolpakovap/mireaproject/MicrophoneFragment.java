package ru.mirea.kolpakovap.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;

public class MicrophoneFragment extends Fragment {
    private final String TAG = "MicrophoneFragment";
    private Button recordButton, playButton;
    private TextView statusText;
    private MediaRecorder recorder = null; // Объект для записи
    private MediaPlayer player = null;     // Объект для проигрывания
    private String fileName = null;        // Путь к аудиофайлу
    private boolean isRecording = false;    // Идет ли сейчас запись?

    // Запрашиваем права на микрофон при входе во фрагмент
    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Log.e(TAG, "Микрофон не разрешен");
                    recordButton.setEnabled(false); // Блокируем кнопку, если нет прав
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_microphone, container, false);
        recordButton = root.findViewById(R.id.buttonRecord);
        playButton = root.findViewById(R.id.buttonPlay);
        statusText = root.findViewById(R.id.textViewStatus);

        // Указываем путь к файлу (папка Музыка внутри нашего приложения)
        fileName = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC), "/note.3gp").getAbsolutePath();

        // Если разрешения нет — просим его
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }

        // Логика кнопки-переключателя (Запись/Стоп)
        recordButton.setOnClickListener(v -> {
            if (!isRecording) {
                startRecording(); // Начинаем
                recordButton.setText("Остановить запись");
                statusText.setText("Идет запись...");
            } else {
                stopRecording(); // Останавливаем
                recordButton.setText("Начать запись");
                statusText.setText("Запись сохранена");
                playButton.setEnabled(true); // Даем послушать
            }
            isRecording = !isRecording; // Меняем флаг
        });

        // Кнопка проигрывания
        playButton.setOnClickListener(v -> startPlaying());
        return root;
    }

    // Метод настройки и старта MediaRecorder
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);      // Источник: Микрофон
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Формат: 3gp (легкий)
        recorder.setOutputFile(fileName);                            // Куда сохранить
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);   // Кодек сжатия
        try {
            recorder.prepare(); // Проверка готовности
            recorder.start();   // СТАРТ
            Log.d(TAG, "Запись началась");
        } catch (IOException e) { e.printStackTrace(); }
    }

    // Метод остановки записи и очистки ресурсов
    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            Log.d(TAG, "Запись завершена, микрофон свободен");
        }
    }

    // Метод настройки и старта MediaPlayer
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName); // Какой файл играть
            player.prepare();               // Подготовка аудиочипа
            player.start();                 // ПУСК
            statusText.setText("Играет...");
            Log.d(TAG, "Проигрывание началось");

            // Когда звук кончится — очищаем память
            player.setOnCompletionListener(mp -> stopPlaying());
        } catch (IOException e) { e.printStackTrace(); }
    }

    // Очистка плеера
    private void stopPlaying() {
        if (player != null) {
            player.release(); // ОБЯЗАТЕЛЬНО: освобождаем память плеера
            player = null;
            statusText.setText("Завершено");
            Log.d(TAG, "Плеер очищен");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Аварийная очистка при закрытии программы
        stopRecording();
        stopPlaying();
    }
}