package com.v1sar.yandextranslator;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.v1sar.yandextranslator.Adapters.TranslatedWordsAdapter;
import com.v1sar.yandextranslator.Data.WordsContract;
import com.v1sar.yandextranslator.Data.WordsDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwerty on 28.03.17.
 */

public class HistoryFragment extends Fragment implements TranslatorFragment.NewData{

    private RecyclerView recyclerView;
    private TranslatedWordsAdapter wAdapter;
    private List<TranslatedWord> wordsList = new ArrayList<>();
    private WordsDbHelper wordsDbHelper;
    private Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.history_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        wAdapter = new TranslatedWordsAdapter(wordsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(wAdapter);
        wordsDbHelper = WordsDbHelper.getInstance(getActivity());
        showDB();
//        btn = (Button) getActivity().findViewById(R.id.buttonTest);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDB();
//            }
//        });
       // prepareData();
    }

    public void showDB() {
        SQLiteDatabase db = wordsDbHelper.getReadableDatabase();
        String[] projection = {
                WordsContract.WordEntry._ID,
                WordsContract.WordEntry.COLUMN_WORD,
                WordsContract.WordEntry.COLUMN_TRANSLATED,
                WordsContract.WordEntry.COLUMN_DIRECTION,
                WordsContract.WordEntry.COLUMN_FAVOURITE};

        Cursor cursor = db.query(
                WordsContract.WordEntry.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        int idColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry._ID);
        int wordColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_WORD);
        int translatedColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_TRANSLATED);
        int dirColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_DIRECTION);
        int favColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_FAVOURITE);

        while (cursor.moveToNext()) {
            int currentID = cursor.getInt(idColumnIndex);
            String currentWord = cursor.getString(wordColumnIndex);
            String currentTranslate = cursor.getString(translatedColumnIndex);
            String currentDir = cursor.getString(dirColumnIndex);
            int currentFav = cursor.getInt(favColumnIndex);
            wordsList.add(new TranslatedWord(currentWord, currentTranslate, currentDir));
        }
        wAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateView() {
        showDB();
    }
//    private void prepareData(){
//        wordsList.add(new TranslatedWord("hello", "lol", "en-ru"));
//        wordsList.add(new TranslatedWord("5hello", "lo1l", "en-ru"));
//        wordsList.add(new TranslatedWord("5hello", "lo1l", "en-ru"));
//        wordsList.add(new TranslatedWord("4hello", "lo2l", "en-ru"));
//        wordsList.add(new TranslatedWord("4hello", "lo2l", "en-ru"));
//        wordsList.add(new TranslatedWord("4hello", "lo2l", "en-ru"));
//        wordsList.add(new TranslatedWord("3hello", "lo3l", "en-ru"));
//        wordsList.add(new TranslatedWord("2hello", "lo4l", "en-ru"));
//        wordsList.add(new TranslatedWord("2hello", "lo4l", "en-ru"));
//        wordsList.add(new TranslatedWord("2hello", "lo4l", "en-ru"));
//        wordsList.add(new TranslatedWord("2hello", "lo4l", "en-ru"));
//        wordsList.add(new TranslatedWord("2hello", "lo4l", "en-ru"));
//        wordsList.add(new TranslatedWord("1hello", "lo5l", "en-ru"));
//        wAdapter.notifyDataSetChanged();
//    }


}
