package com.v1sar.yandextranslator.Views;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.v1sar.yandextranslator.Adapters.FavouriteWordsAdapter;
import com.v1sar.yandextranslator.Adapters.TranslatedWordsAdapter;
import com.v1sar.yandextranslator.Data.WordsContract;
import com.v1sar.yandextranslator.Data.WordsDbHelper;
import com.v1sar.yandextranslator.Helpers.TranslatedWord;
import com.v1sar.yandextranslator.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwerty on 12.04.17.
 */

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavouriteWordsAdapter wAdapter;
    private List<TranslatedWord> wordsListFav = new ArrayList<>();
    private WordsDbHelper wordsDbHelper;
    private Button btn;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourite_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view_fav);
        wAdapter = new FavouriteWordsAdapter(wordsListFav, getActivity());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(wAdapter);
        wordsDbHelper = WordsDbHelper.getInstance(getActivity());
        showDB();
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
                WordsContract.WordEntry.COLUMN_FAVOURITE+"= ?",                  // столбцы для условия WHERE
                new String[] {Integer.toString(1)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                WordsContract.WordEntry._ID + " DESC");                   // порядок сортировки

        int idColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry._ID);
        int wordColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_WORD);
        int translatedColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_TRANSLATED);
        int dirColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_DIRECTION);
        int favColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_FAVOURITE);

        while (cursor.moveToNext()) {
            boolean isFav = false;
            int currentID = cursor.getInt(idColumnIndex);
            String currentWord = cursor.getString(wordColumnIndex);
            String currentTranslate = cursor.getString(translatedColumnIndex);
            String currentDir = cursor.getString(dirColumnIndex);
            int currentFav = cursor.getInt(favColumnIndex);
            if (currentFav == 0) {
                isFav = false;
            } else {
                isFav = true;
            }
            wordsListFav.add(new TranslatedWord(currentWord, currentTranslate, currentDir, isFav));
        }
        wAdapter.notifyDataSetChanged();
    }


    @Subscribe()
    public void onNewWordFavourite(TranslatedWordsAdapter.NewWordFavourite event) {
        wordsListFav.add(0, new TranslatedWord(event.word, event.translation, event.dir, true));
        wAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPosition(0);
    }

    @Subscribe()
    public void onNewWordUnFavourite(FavouriteWordsAdapter.NewWordUnFavourite event) {
        TranslatedWord tempWord = null;
        for(TranslatedWord word : wordsListFav) {
            if (word.getWordToTranslate().equals(event.word) && word.getTranslatedWord().equals(event.translation) && word.getTranslateDirection().equals(event.dir)) {
                tempWord = word;
                break;
            }
        }
        int i = wordsListFav.indexOf(tempWord);
        wordsListFav.remove(i);
        wAdapter.notifyItemRemoved(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
