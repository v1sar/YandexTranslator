package com.v1sar.yandextranslator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by qwerty on 24.03.17.
 */

public class Answer {
        @SerializedName("code")
        @Expose
        private String code;

        @SerializedName("lang")
        @Expose
        private String lang;

        @SerializedName("text")
        @Expose
        private String[] text;

    public void setCode(String code) {
        this.code = code;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public String[] getText() {
        return text;
    }
}
