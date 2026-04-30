package ru.mirea.kolpakovap.mireaproject.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Todo {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("completed")
    @Expose
    public boolean completed;
}