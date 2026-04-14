package ru.mirea.kolpakovap.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    private String phrase;
    public static final String ARG_WORD = "word";

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            // Получаем зашифрованные данные и ключ
            byte[] cryptText = args.getByteArray(ARG_WORD);
            byte[] keyBytes = args.getByteArray("key");

            // Восстанавливаем ключ и расшифровываем
            SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
            phrase = decryptMsg(cryptText, originalKey);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad(); // Обязательный вызов для запуска loadInBackground
    }

    @Nullable
    @Override
    public String loadInBackground() {
        SystemClock.sleep(2000); // Имитация долгой работы
        return phrase;
    }

    // Метод дешифрования
    public static String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}