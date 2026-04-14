package ru.mirea.kolpakovap.mireaproject; // Проверьте, что ваш package совпадает!

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
    private Button recordButton, playButton;
    private TextView statusText;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String fileName = null;
    private boolean isRecording = false;

    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) recordButton.setEnabled(false);
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_microphone, container, false);
        recordButton = root.findViewById(R.id.buttonRecord);
        playButton = root.findViewById(R.id.buttonPlay);
        statusText = root.findViewById(R.id.textViewStatus);

        fileName = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC), "/voice_note.3gp").getAbsolutePath();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }

        recordButton.setOnClickListener(v -> {
            if (!isRecording) {
                startRecording();
                recordButton.setText("Остановить запись");
                statusText.setText("Идет запись...");
            } else {
                stopRecording();
                recordButton.setText("Начать запись");
                statusText.setText("Запись сохранена");
                playButton.setEnabled(true);
            }
            isRecording = !isRecording;
        });

        playButton.setOnClickListener(v -> startPlaying());
        return root;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try { recorder.prepare(); } catch (IOException e) { e.printStackTrace(); }
        recorder.start();
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            statusText.setText("Воспроизведение...");
        } catch (IOException e) { e.printStackTrace(); }
    }
}