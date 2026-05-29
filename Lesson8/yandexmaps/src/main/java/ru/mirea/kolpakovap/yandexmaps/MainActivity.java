package ru.mirea.kolpakovap.yandexmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import ru.mirea.kolpakovap.yandexmaps.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {

    // ViewBinding для доступа к UI элементам без findViewById
    private ActivityMainBinding binding;

    // Объект карты (View, отображающий карту)
    private MapView mapView;

    // Слой для отображения местоположения пользователя на карте
    private UserLocationLayer userLocationLayer;

    // Код запроса для permissions (используется в onRequestPermissionsResult)
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ============================================================
        // ШАГ 1: ИНИЦИАЛИЗАЦИЯ MAPKIT
        // Должна быть вызвана ДО загрузки layout и до создания MapView
        // ============================================================
        MapKitFactory.initialize(this);

        // Инициализация ViewBinding - создает объекты всех View из XML
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Устанавливаем корневой View как контент Activity
        setContentView(binding.getRoot());

        // Получаем ссылку на MapView из разметки (activity_main.xml)
        mapView = binding.mapview;

        // ============================================================
        // ШАГ 2: УСТАНОВКА НАЧАЛЬНОЙ ПОЗИЦИИ КАМЕРЫ
        // Координаты: РТУ МИРЭА (проспект Вернадского, 78)
        // ============================================================
        mapView.getMap().move(
                // CameraPosition - позиция камеры: точка, зум, азимут, угол наклона
                new CameraPosition(new Point(55.670005, 37.479894), 15.0f, 0.0f, 0.0f),
                // Animation - плавная анимация перемещения камеры
                new Animation(Animation.Type.SMOOTH, 0),
                null  // Callback (не используется)
        );

        // ============================================================
        // ШАГ 3: ПРОВЕРКА РАЗРЕШЕНИЙ НА ГЕОЛОКАЦИЮ
        // ============================================================
        checkPermissions();

        // ============================================================
        // ШАГ 4: ЛОГИКА КНОПКИ "ЦЕНТРИРОВАТЬ НА МНЕ"
        // FloatingActionButton для быстрого перемещения к текущей позиции
        // ============================================================
        binding.fabLocation.setOnClickListener(v -> {
            // Проверяем, доступен ли слой местоположения и определена ли позиция
            if (userLocationLayer != null && userLocationLayer.cameraPosition() != null) {
                // Плавно перемещаем камеру к текущему местоположению пользователя
                mapView.getMap().move(
                        new CameraPosition(userLocationLayer.cameraPosition().getTarget(), 16.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null
                );
            } else {
                // Если местоположение не определено - показываем сообщение
                Toast.makeText(this, "Местоположение не определено", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkPermissions() {
        // Проверяем, есть ли уже разрешение на точное местоположение
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Разрешения нет - запрашиваем у пользователя
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
        } else {
            // Разрешение уже есть - загружаем слой местоположения
            loadUserLocationLayer();
        }
    }

    /**
     * ЗАГРУЗКА СЛОЯ МЕСТОПОЛОЖЕНИЯ ПОЛЬЗОВАТЕЛЯ
     * Создает и настраивает слой, который отображает:
     * - Текущую позицию пользователя (синяя точка)
     * - Направление движения (стрелка)
     * - Круг точности (радиус погрешности GPS)
     */
    private void loadUserLocationLayer() {
        // Получаем экземпляр MapKit (синглтон)
        MapKit mapKit = MapKitFactory.getInstance();

        // Создаем слой местоположения, привязанный к окну карты
        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());

        // Делаем слой видимым
        userLocationLayer.setVisible(true);

        // Включаем отображение направления движения (компас/стрелка)
        userLocationLayer.setHeadingEnabled(true);

        // Устанавливаем слушатель событий слоя (onObjectAdded, onObjectRemoved, onObjectUpdated)
        userLocationLayer.setObjectListener(this);
    }

    // ============================================================
    // РЕАЛИЗАЦИЯ ИНТЕРФЕЙСА UserLocationObjectListener
    // ============================================================

    /**
     * Вызывается, когда объект местоположения добавлен на карту
     * Здесь настраивается внешний вид метки пользователя
     */
    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        // Устанавливаем иконку ДЛЯ СТРЕЛКИ (Arrow) - показывает направление движения
        // Используется системная иконка "мое местоположение"
        userLocationView.getArrow().setIcon(ImageProvider.fromResource(this,
                android.R.drawable.ic_menu_mylocation));

        // Устанавливаем иконку ДЛЯ МЕТКИ (Pin) - основная точка на карте
        userLocationView.getPin().setIcon(ImageProvider.fromResource(this,
                android.R.drawable.ic_menu_mylocation));

        // Настраиваем КРУГ ТОЧНОСТИ (Accuracy Circle)
        // Отображает радиус погрешности определения координат
        userLocationView.getAccuracyCircle().setFillColor(Color.argb(30, 0, 0, 255));
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {
    }

    // ============================================================
    // МЕТОДЫ ЖИЗНЕННОГО ЦИКЛА
    // ============================================================

    @Override
    protected void onStop() {
        // Передаем событие onStop в MapView
        mapView.onStop();
        // Передаем событие onStop в MapKitFactory
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Передаем событие onStart в MapKitFactory (порядок важен: сначала фабрика)
        MapKitFactory.getInstance().onStart();
        // Затем передаем в MapView
        mapView.onStart();
    }

    /**
     * ОБРАБОТКА РЕЗУЛЬТАТА ЗАПРОСА РАЗРЕШЕНИЙ
     * Вызывается после того, как пользователь ответил на диалог запроса разрешений
     * @param requestCode код запроса (тот же, что передавали в requestPermissions)
     * @param permissions массив запрошенных разрешений
     * @param grantResults массив результатов (GRANTED или DENIED)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Проверяем, что это наш запрос и разрешение предоставлено
        if (requestCode == PERMISSION_REQUEST_CODE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Разрешение получено - загружаем слой местоположения
            loadUserLocationLayer();
        }
        // Если разрешение не получено - слой не загружается, карта работает без геолокации
    }
}