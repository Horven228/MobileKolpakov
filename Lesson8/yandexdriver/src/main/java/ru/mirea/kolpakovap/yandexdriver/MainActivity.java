package ru.mirea.kolpakovap.yandexdriver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingRouterType;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener {



    // Карта (View для отображения карты)
    private MapView mapView;

    // НАЧАЛЬНАЯ ТОЧКА МАРШРУТА (координаты МИРЭА - пр-т Вернадского, 78)
    private final Point ROUTE_START_LOCATION = new Point(55.670005, 37.479894);

    // КОНЕЧНАЯ ТОЧКА МАРШРУТА (координаты МИРЭА на Сокольниках - ул. Стромынка, 20)
    private final Point ROUTE_END_LOCATION = new Point(55.794229, 37.700772);

    // ПРОМЕЖУТОЧНАЯ ТОЧКА ДЛЯ КРАСНОГО МАРШРУТА
    private final Point RED_WAYPOINT = new Point(55.752710, 37.583287);

    // ПРОМЕЖУТОЧНАЯ ТОЧКА ДЛЯ ЗЕЛЕНОГО МАРШРУТА
    private final Point GREEN_WAYPOINT = new Point(55.715182, 37.686098);

    // Коллекция для хранения графических объектов на карте (линии маршрутов)
    private MapObjectCollection mapObjects;

    // Маршрутизатор для автомобильных маршрутов (Driving - автомобильный)
    private DrivingRouter drivingRouter;

    // Сессия запроса маршрутов (используется для асинхронного запроса)
    private DrivingSession drivingSession;


    private int[] colors = {
            0xFFFF0000,  // КРАСНЫЙ - первый маршрут (проходит через RED_WAYPOINT)
            0xFF00FF00,  // ЗЕЛЕНЫЙ - второй маршрут (проходит через GREEN_WAYPOINT)
            0xFF0000FF   // СИНИЙ   - третий маршрут (прямой)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ============================================================
        // ШАГ 1: ИНИЦИАЛИЗАЦИЯ MAPKIT
        // Должна быть вызвана ДО загрузки layout
        // ============================================================
        MapKitFactory.initialize(this);

        // Устанавливаем разметку activity_main.xml
        setContentView(R.layout.activity_main);

        // ============================================================
        // ШАГ 2: ПОЛУЧЕНИЕ ССЫЛОК НА ОБЪЕКТЫ
        // ============================================================
        // Получаем ссылку на MapView из разметки
        mapView = findViewById(R.id.mapview);

        // Создаем коллекцию для хранения объектов на карте
        // addCollection() создает новую коллекцию, которая может содержать
        // маршруты, маркеры и другие объекты
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        // Создаем маршрутизатор для автомобильных маршрутов
        // DrivingRouterType.COMBINED - использует комбинированные данные (онлайн + кэш)
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED);

        // ============================================================
        // ШАГ 3: ПРОВЕРКА РАЗРЕШЕНИЙ НА ГЕОЛОКАЦИЮ
        // Для построения маршрута от текущего местоположения
        // ============================================================
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Разрешение есть - строим маршрут
            buildRoute();
        } else {
            // Разрешения нет - запрашиваем у пользователя
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }


    // ОБРАБОТКА РЕЗУЛЬТАТА ЗАПРОСА РАЗРЕШЕНИЙ
    // Вызывается после ответа пользователя на диалог разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Разрешение получено - строим маршрут
            buildRoute();
        } else {
            // Разрешение не получено - показываем сообщение
            Toast.makeText(this, "Нужно разрешение для построения маршрута",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void buildRoute() {
        // Перемещаем камеру на начальную точку (МИРЭА)
        // Параметры: позиция, зум (10 - средний масштаб), азимут (0), угол наклона (0)
        mapView.getMap().move(new CameraPosition(ROUTE_START_LOCATION, 10, 0, 0));

        // Отправляем запросы на построение маршрутов
        submitRequestWithWaypoint(ROUTE_START_LOCATION, ROUTE_END_LOCATION, RED_WAYPOINT, 0);   // КРАСНЫЙ - С ПРОМЕЖУТОЧНОЙ ТОЧКОЙ 55.752710, 37.583287
        submitRequestWithWaypoint(ROUTE_START_LOCATION, ROUTE_END_LOCATION, GREEN_WAYPOINT, 1); // ЗЕЛЕНЫЙ - С ПРОМЕЖУТОЧНОЙ ТОЧКОЙ 55.715182, 37.686098
        submitRequest(ROUTE_START_LOCATION, ROUTE_END_LOCATION, 2);                             // СИНИЙ - без промежуточной точки

        // Добавляем маркер в конечной точке
        addMarker(ROUTE_END_LOCATION);
    }

    // Обычный запрос (без промежуточной точки) для синего маршрута
    private void submitRequest(Point start, Point end, int colorIndex) {
        // Настройки маршрута
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        drivingOptions.setRoutesCount(1);  // Запрашиваем 1 маршрут

        // Создаем список точек маршрута (только начало и конец)
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(start, RequestPointType.WAYPOINT, null, null, null));
        requestPoints.add(new RequestPoint(end, RequestPointType.WAYPOINT, null, null, null));

        final int finalColorIndex = colorIndex;

        // Отправляем асинхронный запрос к серверу Яндекс
        drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions,
                new DrivingSession.DrivingRouteListener() {
                    @Override
                    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
                        if (!list.isEmpty()) {
                            mapObjects.addPolyline(list.get(0).getGeometry())
                                    .setStrokeColor(colors[finalColorIndex]);
                        }
                    }

                    @Override
                    public void onDrivingRoutesError(@NonNull com.yandex.runtime.Error error) {
                        // Ошибка
                    }
                });
    }

    // ЗАПРОС С ПРОМЕЖУТОЧНОЙ ТОЧКОЙ (для КРАСНОГО и ЗЕЛЕНОГО маршрутов)
    private void submitRequestWithWaypoint(Point start, Point end, Point waypoint, int colorIndex) {
        // Настройки маршрута
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();
        drivingOptions.setRoutesCount(1);  // Запрашиваем 1 маршрут

        // Создаем список точек маршрута (начало -> промежуточная точка -> конец)
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(start, RequestPointType.WAYPOINT, null, null, null));
        requestPoints.add(new RequestPoint(waypoint, RequestPointType.WAYPOINT, null, null, null));  // ПРОМЕЖУТОЧНАЯ ТОЧКА
        requestPoints.add(new RequestPoint(end, RequestPointType.WAYPOINT, null, null, null));

        final int finalColorIndex = colorIndex;

        // Отправляем асинхронный запрос к серверу Яндекс
        drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions,
                new DrivingSession.DrivingRouteListener() {
                    @Override
                    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
                        if (!list.isEmpty()) {
                            mapObjects.addPolyline(list.get(0).getGeometry())
                                    .setStrokeColor(colors[finalColorIndex]);
                        }
                    }

                    @Override
                    public void onDrivingRoutesError(@NonNull com.yandex.runtime.Error error) {
                        // Ошибка
                    }
                });
    }

    private void addMarker(Point point) {
        // Создаем маркер в указанной точке
        // ImageProvider.fromResource - загружаем иконку из системных ресурсов
        PlacemarkMapObject marker = mapView.getMap().getMapObjects().addPlacemark(
                point,
                ImageProvider.fromResource(this, android.R.drawable.ic_menu_myplaces)
        );

        // Добавляем обработчик нажатия на маркер
        marker.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                // При нажатии на маркер показываем Toast с информацией о заведении
                // В соответствии с заданием: "отображение краткой информации о заведении"
                Toast.makeText(getApplicationContext(),
                        "🏫 МИРЭА НА СОКОЛЬНИКАХ\n📍 Ул. Стромынка, 20",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    // ============================================================
    // РЕАЛИЗАЦИЯ ИНТЕРФЕЙСА DrivingSession.DrivingRouteListener
    // (не используется, так как маршруты строятся через отдельные запросы)
    // ============================================================

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        // Не используется
    }

    @Override
    public void onDrivingRoutesError(@NonNull com.yandex.runtime.Error error) {
        // Не используется
    }

    // ============================================================
    // МЕТОДЫ ЖИЗНЕННОГО ЦИКЛА (ОБЯЗАТЕЛЬНО ДЛЯ MAPKIT)
    // ============================================================

    @Override
    protected void onStop() {
        mapView.onStop();                      // Останавливаем обновление карты
        MapKitFactory.getInstance().onStop();  // Останавливаем MapKit
        super.onStop();
    }


    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();  // Запускаем MapKit
        mapView.onStart();                      // Запускаем обновление карты
    }
}