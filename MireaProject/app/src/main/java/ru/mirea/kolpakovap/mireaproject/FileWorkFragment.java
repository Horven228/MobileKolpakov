package ru.mirea.kolpakovap.mireaproject;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.FileOutputStream;

public class FileWorkFragment extends Fragment {
    public FileWorkFragment() { super(R.layout.fragment_file_work); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab_file);
        EditText etText = view.findViewById(R.id.etFileContent);

        fab.setOnClickListener(v -> {
            // "Шифрование" (сдвиг ASCII на 1) - простая криптография
            String input = etText.getText().toString();
            StringBuilder encrypted = new StringBuilder();
            for (char c : input.toCharArray()) encrypted.append((char) (c + 1));

            try (FileOutputStream fos = requireContext().openFileOutput("secret.txt", Context.MODE_PRIVATE)) {
                fos.write(encrypted.toString().getBytes());
                Toast.makeText(getContext(), "Зашифровано шифром Цезаря и сохранено в secret.txt", Toast.LENGTH_SHORT).show();
            } catch (Exception e) { e.printStackTrace(); }
        });
    }
}