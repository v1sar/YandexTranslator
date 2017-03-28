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

    public String convert(String spinner) {
        return languageToCode.get(spinner);
    }


}
