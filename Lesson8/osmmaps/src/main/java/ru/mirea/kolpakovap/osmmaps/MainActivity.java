package ru.mirea.kolpakovap.osmmaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.kolpakovap.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // ============================================================
    // ПОЛЯ КЛАССА
    // ============================================================

    // Объект карты OpenStreetMap
    private MapView mapView = null;

    // ViewBinding для доступа к UI элементам
    private ActivityMainBinding binding;

    // Слой для отображения местоположения пользователя
    private MyLocationNewOverlay locationNewOverlay;

    /**
     * Метод onCreate вызывается при создании Activity
     * @param savedInstanceState сохраненное состояние Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ============================================================
        // ШАГ 1: ИНИЦИАЛИЗАЦИЯ OSMDRID
        // Загрузка конфигурации (кэш, настройки сети)
        // ============================================================
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx,
                PreferenceManager.getDefaultSharedPreferences(ctx));

        // ============================================================
        // ШАГ 2: ИНИЦИАЛИЗАЦИЯ VIEWBINDING И КАРТЫ
        // ============================================================
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получаем ссылку на MapView из разметки
        mapView = binding.mapview;

        // ============================================================
        // ШАГ 3: НАСТРОЙКА КАРТЫ
        // ============================================================

        // Включаем округление зума (для плавного приближения/отдаления)
        mapView.setZoomRounding(true);

        // Включаем мультитач (масштабирование двумя пальцами)
        // "кнопки масштабирования и увеличение двумя пальцами"
        mapView.setMultiTouchControls(true);

        // Получаем контроллер карты для управления камерой
        IMapController mapController = mapView.getController();

        // Устанавливаем уровень зума (11.0 - средний масштаб, видно район)
        mapController.setZoom(11.0);

        // ============================================================
        // ШАГ 4: УСТАНОВКА ЦЕНТРА КАРТЫ
        // Ставим центр карты примерно посередине между двумя точками:
        // - МИРЭА на Вернадке (55.670005, 37.479894)
        // - МИРЭА на Сокольниках (55.794229, 37.700772)
        // Центр: примерно (55.7321, 37.5888)
        // ============================================================
        GeoPoint centerPoint = new GeoPoint(55.7321, 37.5888);
        mapController.setCenter(centerPoint);

        // ============================================================
        // ШАГ 5: ПРОВЕРКА РАЗРЕШЕНИЙ НА ГЕОЛОКАЦИЮ
        // ============================================================
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Разрешение есть - добавляем слои на карту
            addOverlays();
        } else {
            // Разрешения нет - запрашиваем у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    // Метод для добавления всех слоев и оверлеев на карту (методичка стр. 21-22)
    private void addOverlays() {

        // ============================================================
        // 1. СЛОЙ МЕСТОПОЛОЖЕНИЯ ПОЛЬЗОВАТЕЛЯ
        // Создаем слой с определением местоположения через GPS
        locationNewOverlay = new MyLocationNewOverlay(
                new GpsMyLocationProvider(this),  // Провайдер GPS
                mapView                           // Карта, на которую добавляем
        );

        // Включаем отображение текущего местоположения
        locationNewOverlay.enableMyLocation();

        // Добавляем слой на карту
        mapView.getOverlays().add(locationNewOverlay);

        // 2. КОМПАС
        // Создаем слой компаса (использует внутренний сенсор ориентации)
        CompassOverlay compassOverlay = new CompassOverlay(
                this,                                    // Контекст
                new InternalCompassOrientationProvider(this),  // Провайдер компаса
                mapView                                  // Карта
        );

        // Включаем отображение компаса
        compassOverlay.enableCompass();

        // Добавляем слой компаса на карту
        mapView.getOverlays().add(compassOverlay);

        // ============================================================
        // 3. МЕТРИЧЕСКАЯ ШКАЛА МАСШТАБА
        // Получаем параметры экрана (для позиционирования шкалы)
        final DisplayMetrics dm = this.getResources().getDisplayMetrics();

        // Создаем слой шкалы масштаба
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);

        // Центрируем шкалу
        scaleBarOverlay.setCentred(true);

        // Устанавливаем позицию шкалы: центр экрана по горизонтали, 10px от верхнего края
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);

        // Добавляем шкалу на карту
        mapView.getOverlays().add(scaleBarOverlay);

        // ============================================================
        // 4. МАРКЕР 1: МИРЭА НА СОКОЛЬНИКАХ
        Marker marker1 = new Marker(mapView);

        // Устанавливаем позицию маркера (Сокольники)
        marker1.setPosition(new GeoPoint(55.794229, 37.700772));

        // Устанавливаем обработчик нажатия на маркер
        marker1.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                // Показываем Toast с информацией о месте
                Toast.makeText(getApplicationContext(),
                        "🏫 МИРЭА НА СОКОЛЬНИКАХ\n📍 ул. Стромынка, 20",
                        Toast.LENGTH_SHORT).show();
                return true;  // true - событие обработано
            }
        });

        // Устанавливаем иконку маркера (из стандартных ресурсов osmdroid)
        marker1.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));

        // Устанавливаем заголовок маркера (всплывает при нажатии)
        marker1.setTitle("МИРЭА на Сокольниках");

        // Добавляем маркер на карту
        mapView.getOverlays().add(marker1);

        // --- МАРКЕР 2 (МИРЭА на Вернадке) ---
        Marker marker2 = new Marker(mapView);

        // Устанавливаем позицию маркера (Вернадка)
        marker2.setPosition(new GeoPoint(55.670005, 37.479894));

        // Устанавливаем обработчик нажатия на маркер
        marker2.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                // Показываем Toast с информацией о месте
                Toast.makeText(getApplicationContext(),
                        "🏛️ РТУ МИРЭА НА ВЕРНАДКЕ\n📍 пр-т Вернадского, 78",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Устанавливаем иконку маркера
        marker2.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));

        // Устанавливаем заголовок маркера
        marker2.setTitle("РТУ МИРЭА");

        // Добавляем маркер на карту
        mapView.getOverlays().add(marker2);
    }

    // Обработка результата запроса разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addOverlays();  // Разрешение получено - добавляем слои
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(this, PreferenceManager.getDefaultSharedPreferences(this));
        if (mapView != null) mapView.onPause();
    }
}