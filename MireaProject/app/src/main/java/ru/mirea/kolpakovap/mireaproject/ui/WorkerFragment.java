package ru.mirea.kolpakovap.mireaproject.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.kolpakovap.mireaproject.ui.DataWorker;
import ru.mirea.kolpakovap.mireaproject.databinding.FragmentWorkerBinding;

public class WorkerFragment extends Fragment {

    private FragmentWorkerBinding binding;

    public WorkerFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Инициализация View Binding для фрагмента
        binding = FragmentWorkerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Обработка нажатия на кнопку
        binding.buttonStartChain.setOnClickListener(v -> {
            // Создаем два запроса (имитация цепочки задач)
            OneTimeWorkRequest workRequest1 = new OneTimeWorkRequest.Builder(DataWorker.class).build();
            OneTimeWorkRequest workRequest2 = new OneTimeWorkRequest.Builder(DataWorker.class).build();

            // Запускаем цепочку: сначала первая задача, потом вторая
            WorkManager.getInstance(requireContext())
                    .beginWith(workRequest1)
                    .then(workRequest2)
                    .enqueue();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Обнуляем binding, чтобы избежать утечек памяти (требование стр. 6 практики)
        binding = null;
    }
}