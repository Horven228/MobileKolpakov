package ru.mirea.kolpakovap.mireaproject;

// Импорты для диалогового окна
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

// AndroidX компоненты
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Яндекс Карты (MapKit) - библиотека для работы с картами
import com.yandex.mapkit.Animation;           // Анимация перемещения камеры
import com.yandex.mapkit.MapKitFactory;       // Основной класс для инициализации карт
import com.yandex.mapkit.geometry.Point;       // Координаты (широта, долгота)
import com.yandex.mapkit.map.CameraPosition;  // Позиция камеры (центр, масштаб)
import com.yandex.mapkit.map.MapObject;        // Базовый класс для объектов на карте
import com.yandex.mapkit.map.MapObjectCollection; // Коллекция объектов на карте
import com.yandex.mapkit.map.MapObjectTapListener; // Обработчик нажатия на объект
import com.yandex.mapkit.map.PlacemarkMapObject;   // Маркер на карте
import com.yandex.mapkit.mapview.MapView;          // Виджет карты
import com.yandex.runtime.image.ImageProvider;     // Загрузка иконки для маркера

import java.util.ArrayList;
import java.util.List;


public class PlacesFragment extends Fragment {

    // Виджет карты (View из fragment_places.xml)
    private MapView mapView;

    // Коллекция для хранения всех объектов на карте (маркеры, линии)
    private MapObjectCollection mapObjects;

    // Список всех заведений (хранит название, адрес, описание, координаты)
    private List<PlaceInfo> placesList = new ArrayList<>();

    // Индекс текущего заведения (для кнопки переключения)
    private int currentIndex = 0;

    // Кнопка "Следующее заведение" (дополнительная функция)
    private Button buttonNextPlace;


    // Внутренний класс для хранения информации о заведении
    // Используется для связывания маркера с данными через setUserData()

    private static class PlaceInfo {
        String title;        // Название заведения
        String address;      // Адрес
        String description;  // Описание
        Point point;         // Координаты на карте

        PlaceInfo(String title, String address, String description, Point point) {
            this.title = title;
            this.address = address;
            this.description = description;
            this.point = point;
        }
    }


     // Обработчик нажатия на маркер
     // mapObject - объект на карте (маркер)
     // point - координаты точки нажатия

    private final MapObjectTapListener placeTapListener = (mapObject, point) -> {
        // Проверяем, что в объекте есть данные о заведении
        if (mapObject.getUserData() instanceof PlaceInfo) {
            PlaceInfo info = (PlaceInfo) mapObject.getUserData();

            // Создаём и показываем диалоговое окно
            new AlertDialog.Builder(requireContext())
                    .setTitle(info.title)                          // Заголовок
                    .setMessage("Адрес: " + info.address + "\n\nОписание:\n" + info.description) // Текст
                    .setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss()) // Кнопка
                    .setIcon(android.R.drawable.ic_dialog_info)   // Иконка
                    .show();
        }
        return true; // Событие обработано
    };


     // onCreate - вызывается при создании фрагмента
     // Здесь инициализируется Яндекс Карты

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация библиотеки карт (обязательно до использования MapView)
        MapKitFactory.initialize(requireContext());
    }


     // onCreateView - создание UI фрагмента
     // Здесь создаются все View (карта, кнопка)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Загружаем layout fragment_places.xml
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        // Находим карту по ID
        mapView = view.findViewById(R.id.yandexMapView);
        // Находим кнопку по ID
        buttonNextPlace = view.findViewById(R.id.buttonNextPlace);

        // Настройка карты (начальная позиция камеры)
        setupMap();

        // Добавление маркеров заведений
        addEstablishmentMarkers();


        // ДОПОЛНИТЕЛЬНАЯ ФУНКЦИЯ
        // Кнопка "Следующее заведение" - последовательное переключение камеры
        // При каждом нажатии камера перемещается к следующему заведению
        buttonNextPlace.setOnClickListener(v -> {
            if (!placesList.isEmpty()) {
                // Переключаем индекс по кругу (циклически)
                currentIndex = (currentIndex + 1) % placesList.size();
                PlaceInfo nextPlace = placesList.get(currentIndex);

                // Плавно перемещаем камеру к выбранному заведению
                // CameraPosition: цель, масштаб (15 - детальный вид), наклон, поворот
                // Animation: плавная анимация перелёта
                mapView.getMap().move(
                        new CameraPosition(nextPlace.point, 15.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0.5f),
                        null);

                // Всплывающее сообщение с номером и названием заведения
                Toast.makeText(getContext(),
                        (currentIndex + 1) + "/" + placesList.size() + ": " + nextPlace.title,
                        Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Настройка карты: начальная позиция камеры
    // CameraPosition параметры:
    private void setupMap() {
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 12.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),  // Без анимации
                null);
        // Создаём коллекцию для объектов на карте
        mapObjects = mapView.getMap().getMapObjects().addCollection();
    }

    // Добавление маркеров заведений на карту
    // Создаёт 3 заведения с названием, адресом, описанием и координатами

    private void addEstablishmentMarkers() {
        // Заведение 1: Кафе "Уют" (Тверская улица)
        Point point1 = new Point(55.751574, 37.573856);
        PlaceInfo info1 = new PlaceInfo(
                "Кафе 'Уют'",
                "ул. Тверская, 15",
                "Уютное кафе с домашней кухней и свежей выпечкой. Отличное место для завтрака или обеда.",
                point1
        );
        placesList.add(info1);
        createMarker(point1, info1);

        // Заведение 2: Ресторан "Прага" (Арбат)
        Point point2 = new Point(55.758551, 37.614979);
        PlaceInfo info2 = new PlaceInfo(
                "Ресторан 'Прага'",
                "Арбат, 2/1",
                "Классическая русская и европейская кухня. Исторический ресторан в центре Москвы.",
                point2
        );
        placesList.add(info2);
        createMarker(point2, info2);

        // Заведение 3: Кофейня "Кофеин" (Большая Никитская)
        Point point3 = new Point(55.760246, 37.603456);
        PlaceInfo info3 = new PlaceInfo(
                "Кофейня 'Кофеин'",
                "ул. Большая Никитская, 22",
                "Лучший specialty кофе в городе. Домашние десерты и уютная атмосфера.",
                point3
        );
        placesList.add(info3);
        createMarker(point3, info3);
    }

    // Создание маркера
    private void createMarker(Point point, PlaceInfo info) {
        // Создаём маркер в коллекции с иконкой-звездочкой
        PlacemarkMapObject mark = mapObjects.addPlacemark(
                point,
                ImageProvider.fromResource(requireContext(), android.R.drawable.btn_star_big_on)
        );
        // Сохраняем данные о заведении в маркере (чтобы потом обработать нажатие)
        mark.setUserData(info);
        // Устанавливаем обработчик нажатия на маркер
        mark.addTapListener(placeTapListener);
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();                          // Запуск отрисовки карты
        MapKitFactory.getInstance().onStart();      // Запуск движка карт
    }


    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}