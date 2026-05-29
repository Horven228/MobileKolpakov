package ru.mirea.kolpakovap.mireaproject;

// Импорт для работы с Intent (переход между экранами)
import android.content.Intent;
// Импорт для работы с жизненным циклом активности
import android.os.Bundle;
// Импорт для всплывающих сообщений
import android.widget.Toast;
// Базовый класс для Activity с поддержкой ActionBar/Toolbar
import androidx.appcompat.app.AppCompatActivity;
// Класс Firebase для аутентификации пользователей
import com.google.firebase.auth.FirebaseAuth;
// Класс Firebase для получения информации о текущем пользователе
import com.google.firebase.auth.FirebaseUser;
// Сгенерированный класс для ViewBinding (связь с activity_auth.xml)
import ru.mirea.kolpakovap.mireaproject.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    // ViewBinding - объект для удобного доступа ко всем элементам разметки
    // Позволяет обращаться к кнопкам и полям ввода через binding.имя_элемента
    private ActivityAuthBinding binding;

    // FirebaseAuth - основной класс для работы с аутентификацией
    // Предоставляет методы: вход, регистрация, выход, сброс пароля
    private FirebaseAuth mAuth;


    // onCreate вызывается при создании активности
    // Здесь происходит инициализация интерфейса и настройка обработчиков кнопок

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Инициализация ViewBinding
        // inflate - создает объект Binding на основе layout-файла
        // getLayoutInflater() - системный сервис для преобразования XML в View
        binding = ActivityAuthBinding.inflate(getLayoutInflater());

        // 2. Устанавливаем корневой View как содержимое экрана
        // getRoot() возвращает корневой элемент из activity_auth.xml
        setContentView(binding.getRoot());

        // 3. Инициализация Firebase Auth
        // getInstance() возвращает единственный экземпляр FirebaseAuth (паттерн Singleton)
        mAuth = FirebaseAuth.getInstance();

        // АВТОМАТИЧЕСКИЙ ВХОД (закомментирован)
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // if (currentUser != null) {
        //     goToMainActivity();
        // }

        // КНОПКА "ВОЙТИ" - аутентификация существующего пользователя
        // setOnClickListener - устанавливаем обработчик нажатия на кнопку
        // v - это сама кнопка (View), на которую нажали
        binding.signInButton.setOnClickListener(v -> {
            // Получаем текст из поля Email и удаляем лишние пробелы по краям
            String email = binding.emailEditText.getText().toString().trim();
            // Получаем текст из поля Password
            String password = binding.passwordEditText.getText().toString().trim();

            // Проверка на пустые поля
            if (email.isEmpty() || password.isEmpty()) {
                // Toast - всплывающее сообщение (появляется на секунду и исчезает)
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return; // Прерываем выполнение, не отправляем запрос
            }

            // Отправляем запрос на вход в Firebase
            // signInWithEmailAndPassword - асинхронный метод, не блокирует UI
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        // Этот код выполнится, когда Firebase ответит (успех/ошибка)
                        if (task.isSuccessful()) {
                            // Успешный вход - переходим на главный экран
                            goToMainActivity();
                        } else {
                            // Ошибка входа - показываем сообщение
                            Toast.makeText(AuthActivity.this, "Ошибка входа", Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // ============================================================
        // КНОПКА "ЗАРЕГИСТРИРОВАТЬСЯ" - создание нового пользователя
        // ============================================================
        binding.createAccountButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            // Проверка на пустые поля
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            // Отправляем запрос на регистрацию нового пользователя в Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Регистрация успешна - показываем сообщение
                            Toast.makeText(AuthActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                            // Переходим на главный экран (пользователь уже автоматически залогинен)
                            goToMainActivity();
                        } else {
                            // Ошибка регистрации (например, email уже существует)
                            Toast.makeText(AuthActivity.this, "Ошибка регистрации", Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    // Переход на главный экран (MainActivity)

    private void goToMainActivity() {
        // Создаем Intent для перехода из AuthActivity в MainActivity
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        // Запускаем MainActivity
        startActivity(intent);
        // Закрываем AuthActivity, удаляем её из стека активностей
        finish();
    }
}