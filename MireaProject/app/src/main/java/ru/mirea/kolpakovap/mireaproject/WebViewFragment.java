package ru.mirea.kolpakovap.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WebViewFragment extends Fragment {
    // Это «менеджер» экрана с браузером. Он управляет элементом из fragment_web_view.xml.
    public WebViewFragment() {
        // Обязательный пустой конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Надуваем макет фрагмента (fragment_web_view.xml)
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Находим WebView в разметке по ID
        WebView webView = view.findViewById(R.id.webView);

        // 2. Настройка WebViewClient, чтобы ссылки открывались ВНУТРИ приложения,
        // а не выкидывали пользователя в обычный браузер Chrome
        webView.setWebViewClient(new WebViewClient());

        // 3. Разрешаем использование JavaScript (нужно для работы Google и других сайтов)
        webView.getSettings().setJavaScriptEnabled(true);

        // 4. Загружаем страницу (как указано в задании на стр. 31)
        webView.loadUrl("https://www.google.com");
    }
}