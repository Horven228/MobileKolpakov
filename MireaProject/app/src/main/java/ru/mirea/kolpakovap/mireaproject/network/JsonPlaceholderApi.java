package ru.mirea.kolpakovap.mireaproject.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceholderApi {
    @GET("todos/1")
    Call<Todo> getTodo();
}