package com.v1sar.yandextranslator.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by qwerty on 12.04.17.
 */

public class PreferenceHelper {


    private static PreferenceHelper instance;

    private Context context;

    private SharedPreferences preferences;

    private PreferenceHelper() {

    }

    public static PreferenceHelper getInstance() {
        if(instance==null) {
            instance = new PreferenceHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    public void putInteger(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public int getInteger(String key) {
        return preferences.getInt(key, 0);
    }

}
