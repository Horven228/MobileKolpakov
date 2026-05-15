package ru.mirea.kolpakovap.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private ImageView imageView;
    private Uri imageUri;
    private boolean isWork = false;

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
        // Находим ImageView в разметке
        imageView = findViewById(R.id.imageView);

        // 1. Проверка разрешений в реальном времени
        // Проверяем, даны ли права на Камеру и Запись во внешнюю память
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        // 2. Обработчик результата (когда камера сделала фото)
        // Инициализируем лаунчер, который будет ждать возврата из приложения "Камера"
        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Если пользователь сделал фото и нажал "ОК"
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Устанавливаем сохраненное изображение в наш ImageView
                        imageView.setImageURI(imageUri);
                    }
                });

        // 3. Запуск камеры по нажатию на ImageView
        // При нажатии на ImageView запускается процесс фотографирования
        imageView.setOnClickListener(v -> {
            // Создаем намерение: "Я хочу сделать снимок"
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Проверяем, подтвердил ли пользователь разрешения
            if (isWork) {
                try {
                    // Создаем пустой файл для будущего фото
                    File photoFile = createImageFile();

                    // Генерируем безопасный URI для передачи стороннему приложению (Камере)
                    // authorities должен совпадать с тем, что указан в AndroidManifest.xml
                    String authorities = getPackageName() + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(MainActivity.this, authorities, photoFile);

                    // Передаем камере путь, по которому она должна сохранить снимок
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    // Запускаем камеру через лаунчер
                    cameraActivityResultLauncher.launch(cameraIntent);
                } catch (IOException e) {
                    e.printStackTrace(); // Обработка ошибок при создании файла
                }
            }
        });
    }

    // Вспомогательный метод для создания пустого .jpg файла в памяти телефона.
    // Имя файла формируется на основе текущей даты и времени.
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";

        // Получаем путь к папке "Pictures" внутри хранилища приложения
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Создаем временный файл
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

    // Метод вызывается после того, как пользователь нажал "Разрешить" или "Запретить" в окне прав
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            // Проверяем, что первый элемент массива (камера) разрешен
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}