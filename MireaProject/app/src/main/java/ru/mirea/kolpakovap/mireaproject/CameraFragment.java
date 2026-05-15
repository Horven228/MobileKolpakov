package ru.mirea.kolpakovap.mireaproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment {
    private final String TAG = "CameraFragment";
    private ImageView avatarImage; // Рамка для фото
    private Uri imageUri;          // Путь к сохраненному фото

    // Лаунчер для обработки фото после того, как камера закроется
    private ActivityResultLauncher<Intent> cameraLauncher;
    // Лаунчер для вызова системного окна разрешений
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_camera, container, false);
        avatarImage = root.findViewById(R.id.imageViewAvatar);

        // Инициализируем лаунчер получения результата от камеры
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Если фото сделано — ставим его в ImageView
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        avatarImage.setImageURI(imageUri);
                        Log.d(TAG, "Фото успешно установлено");
                    }
                });

        // Инициализируем лаунчер для запроса разрешения
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    // Если юзер разрешил — запускаем метод съемки
                    if (isGranted) {
                        dispatchTakePictureIntent();
                    } else {
                        Log.w(TAG, "Доступ к камере запрещен");
                        Toast.makeText(getContext(), "Нужно разрешение!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Кнопка "Сделать фото"
        root.findViewById(R.id.buttonMakePhoto).setOnClickListener(v -> {
            // Проверяем: есть ли уже права на камеру?
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                // Если нет — запускаем окно запроса
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        return root;
    }

    // Метод подготовки приказа для открытия камеры
    private void dispatchTakePictureIntent() {
        Log.d(TAG, "Запуск камеры...");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            // Создаем пустой файл под будущий снимок
            File photoFile = createImageFile();
            // Генерируем безопасный путь через FileProvider
            String authorities = requireContext().getPackageName() + ".fileprovider";
            imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
            // Сохраним результат вот по этому адресу
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraLauncher.launch(cameraIntent);
        } catch (IOException e) {
            Log.e(TAG, "Ошибка файла", e);
        }
    }

    // Метод генерации пустого .jpg файла в памяти телефона
    private File createImageFile() throws IOException {
        // Имя файла на основе текущего времени (чтобы не повторялись)
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        // Путь к папке картинок нашего приложения
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Создаем сам файл
        return File.createTempFile("PHOTO_" + timeStamp, ".jpg", storageDir);
    }
}