package ru.mirea.kolpakovap.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;

public class MicrophoneFragment extends Fragment {

    private Button recordButton, playButton;
    private TextView statusText;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String fileName = null;
    private boolean isRecording = false;

    private final ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    recordButton.setEnabled(false);
                    Toast.makeText(getContext(), "Нет разрешения на запись", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);

        recordButton = view.findViewById(R.id.buttonRecord);
        playButton = view.findViewById(R.id.buttonPlay);
        statusText = view.findViewById(R.id.textViewStatus);

        fileName = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC), "audio_note.3gp").getAbsolutePath();

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

        return view;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            player.setOnCompletionListener(mp -> {
                statusText.setText("Завершено");
                stopPlaying();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
        stopPlaying();
    }
}