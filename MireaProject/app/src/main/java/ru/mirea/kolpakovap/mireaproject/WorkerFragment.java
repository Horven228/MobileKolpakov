package ru.mirea.kolpakovap.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class WorkerFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        Button btnStart = view.findViewById(R.id.btnStartWorker);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем запрос на разовое выполнение нашей задачи
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();

                // Отправляем менеджеру
                WorkManager.getInstance(requireContext()).enqueue(workRequest);

                Toast.makeText(getContext(), "Задача запущена! Смотри Logcat", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
