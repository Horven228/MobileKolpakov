package ru.mirea.kolpakovap.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import ru.mirea.kolpakovap.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGetData.setOnClickListener(v -> {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();

            if (networkinfo != null && networkinfo.isConnected()) {
                new DownloadIpTask().execute("https://ipinfo.io/json");
            } else {
                Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- TASK 1: Получение информации по IP ---
    private class DownloadIpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);
                String ip = responseJson.getString("ip");
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String loc = responseJson.getString("loc"); // Пример: "55.7522,37.6156"

                binding.tvIP.setText("IP: " + ip);
                binding.tvCity.setText("Город: " + city);
                binding.tvRegion.setText("Регион: " + region);
                binding.tvLoc.setText("Координаты: " + loc);

                // РАЗДЕЛЯЕМ loc НА ШИРОТУ И ДОЛГОТУ
                String[] coords = loc.split(",");
                String latitude = coords[0];
                String longitude = coords[1];

                // ЗАПУСКАЕМ ВТОРОЙ ЗАПРОС (ПОГОДА)
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                        "&longitude=" + longitude + "&current_weather=true";
                new DownloadWeatherTask().execute(weatherUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // --- TASK 2: Получение Погоды ---
    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]); // Используем тот же метод скачивания
            } catch (IOException e) {
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                JSONObject current = json.getJSONObject("current_weather");
                String temp = current.getString("temperature");
                binding.tvWeather.setText("Температура: " + temp + "°C");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Общий метод для HttpURLConnection (стр. 16 методички)
    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                return bos.toString();
            } else {
                return connection.getResponseMessage();
            }
        } finally {
            if (inputStream != null) inputStream.close();
        }
    }
}