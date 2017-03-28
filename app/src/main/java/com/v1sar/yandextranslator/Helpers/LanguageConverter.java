package com.v1sar.yandextranslator.Helpers;

import android.content.Context;

import com.v1sar.yandextranslator.R;

import java.util.HashMap;

/**
 * Created by qwerty on 24.03.17.
 */

public class LanguageConverter {
    private static final LanguageConverter instance = new LanguageConverter();

    private static HashMap<String, String> languageToCode;

    private LanguageConverter() {
        languageToCode = new HashMap<>();
    }

    public static LanguageConverter getInstance() {
        return instance;
    }

    private void initializeMap(Context context) {
       String [] languages = context.getResources().getStringArray(R.array.languages);
       String [] languagesCodes = context.getResources().getStringArray(R.array.languageCodes);
        for (int i = 0; i < languages.length; i++) {
            languageToCode.put(languages[i], languagesCodes[i]);
        }
    }

    public String convert(Context context, String spinner) {
        if (languageToCode.isEmpty())
            initializeMap(context);
        return languageToCode.get(spinner);
    }


}
