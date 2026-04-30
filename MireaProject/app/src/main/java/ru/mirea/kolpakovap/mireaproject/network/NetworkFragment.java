package ru.mirea.kolpakovap.mireaproject.network;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.kolpakovap.mireaproject.databinding.FragmentNetworkBinding;
import ru.mirea.kolpakovap.mireaproject.network.JsonPlaceholderApi;
import ru.mirea.kolpakovap.mireaproject.network.Todo;

public class NetworkFragment extends Fragment {
    private FragmentNetworkBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNetworkBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFetch.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceholderApi api = retrofit.create(JsonPlaceholderApi.class);

            api.getTodo().enqueue(new Callback<Todo>() {
                @Override
                public void onResponse(Call<Todo> call, Response<Todo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.textViewResult.setText("Title: " + response.body().title);
                    }
                }

                @Override
                public void onFailure(Call<Todo> call, Throwable t) {
                    binding.textViewResult.setText("Ошибка: " + t.getMessage());
                }
            });
        });
    }
}
