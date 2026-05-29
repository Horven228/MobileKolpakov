package ru.mirea.kolpakovap.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;

public class AudioFragment extends Fragment {
    private static final String TAG = "AudioFragment";
    private Button recordButton, playButton;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String recordFilePath = null;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;
    private boolean isWork = false;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                isWork = isGranted;
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        recordButton = view.findViewById(R.id.btnRecord);
        playButton = view.findViewById(R.id.btnPlay);

        recordFilePath = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp").getAbsolutePath();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }

        recordButton.setOnClickListener(v -> {
            if (!isWork) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                return;
            }
            if (isStartRecording) {
                recordButton.setText("Остановить запись");
                playButton.setEnabled(false);
                startRecording();
            } else {
                recordButton.setText("Начать запись");
                playButton.setEnabled(true);
                stopRecording();
            }
            isStartRecording = !isStartRecording;
        });

        playButton.setOnClickListener(v -> {
            if (isStartPlaying) {
                playButton.setText("Остановить воспроизведение");
                recordButton.setEnabled(false);
                startPlaying();
            } else {
                playButton.setText("Воспроизвести");
                recordButton.setEnabled(true);
                stopPlaying();
            }
            isStartPlaying = !isStartPlaying;
        });

        return view;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
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
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
            // Сброс кнопки после завершения проигрывания
            player.setOnCompletionListener(mp -> {
                playButton.setText("Воспроизвести");
                recordButton.setEnabled(true);
                isStartPlaying = true;
                stopPlaying();
            });
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
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