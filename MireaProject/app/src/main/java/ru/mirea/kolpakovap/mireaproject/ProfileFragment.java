package ru.mirea.kolpakovap.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextHobby;
    private SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextAge = view.findViewById(R.id.editTextAge);
        editTextHobby = view.findViewById(R.id.editTextHobby);
        Button buttonSave = view.findViewById(R.id.buttonSaveProfile);

        sharedPref = requireActivity().getSharedPreferences("MireaProjectPrefs", Context.MODE_PRIVATE);

        // Загрузка сохранённых данных
        editTextName.setText(sharedPref.getString("NAME", ""));
        editTextAge.setText(sharedPref.getString("AGE", ""));
        editTextHobby.setText(sharedPref.getString("HOBBY", ""));

        buttonSave.setOnClickListener(v -> saveData());

        return view;
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("NAME", editTextName.getText().toString());
        editor.putString("AGE", editTextAge.getText().toString());
        editor.putString("HOBBY", editTextHobby.getText().toString());
        editor.apply();
        Toast.makeText(getContext(), "Данные профиля успешно сохранены", Toast.LENGTH_SHORT).show();
    }
}