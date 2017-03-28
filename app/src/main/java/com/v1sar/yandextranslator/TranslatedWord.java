package com.v1sar.yandextranslator;

/**
 * Created by qwerty on 28.03.17.
 */

public class TranslatedWord {
    private String wordToTranslate;
    private String translatedWord;
    private String translateDirection;

    public TranslatedWord() {
    }

    public TranslatedWord(String wordToTranslate, String translatedWord, String translateDirection) {
        this.wordToTranslate = wordToTranslate;
        this.translatedWord = translatedWord;
        this.translateDirection = translateDirection;
    }

    public void setWordToTranslate(String wordToTranslate) {
        this.wordToTranslate = wordToTranslate;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public void setTranslateDirection(String translateDirection) {
        this.translateDirection = translateDirection;
    }

    public String getWordToTranslate() {
        return wordToTranslate;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public String getTranslateDirection() {
        return translateDirection;
    }
}
