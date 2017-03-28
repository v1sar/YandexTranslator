package com.v1sar.yandextranslator.Data;

import android.provider.BaseColumns;

/**
 * Created by qwerty on 28.03.17.
 */

public class WordsContract {

    private WordsContract() {}

    public static final class WordEntry implements BaseColumns {
        public final static String TABLE_NAME = "translated_words";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_WORD = "word";
        public final static String COLUMN_TRANSLATED = "translated";
        public final static String COLUMN_DIRECTION = "direction";
        public final static String COLUMN_FAVOURITE = "favourite";

        public static final int FAVOURITE_FALSE = 0;
        public static final int FAVOURITE_TRUE = 1;
    }

}
