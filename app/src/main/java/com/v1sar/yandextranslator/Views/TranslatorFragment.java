package com.v1sar.yandextranslator.Views;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
//import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.v1sar.yandextranslator.Helpers.PreferenceHelper;
import com.v1sar.yandextranslator.Helpers.TranslatedWord;
import com.v1sar.yandextranslator.Internet.Answer;
import com.v1sar.yandextranslator.Internet.ApiService;
import com.v1sar.yandextranslator.Data.WordsContract;
import com.v1sar.yandextranslator.Data.WordsDbHelper;
import com.v1sar.yandextranslator.Helpers.LanguageConverter;
import com.v1sar.yandextranslator.R;
import com.v1sar.yandextranslator.Internet.RetroClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qwerty on 28.03.17.
 */

public class TranslatorFragment extends Fragment {

    private static String API_KEY;

    Button btnTranslate;
    EditText txtEdit;
    TextView txtTranslated;
    SearchableSpinner leftSpinner;
    SearchableSpinner rightSpinner;
    WordsDbHelper wordsDbHelper;
    PreferenceHelper preferenceHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.translator_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        API_KEY = getResources().getString(R.string.API_KEY);
        PreferenceHelper.getInstance().init(getActivity());
        preferenceHelper = PreferenceHelper.getInstance();
        txtTranslated = (TextView) getActivity().findViewById(R.id.txt_translated);
        btnTranslate = (Button) getActivity().findViewById(R.id.btn_translate);
        txtEdit = (EditText) getActivity().findViewById(R.id.text_to_translate);
        leftSpinner = (SearchableSpinner) getActivity().findViewById(R.id.left_spinner);
        rightSpinner = (SearchableSpinner) getActivity().findViewById(R.id.right_spinner);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(txtEdit.getText().toString());
            }
        });
        wordsDbHelper = WordsDbHelper.getInstance(getActivity());
        leftSpinner.setSelection(preferenceHelper.getInteger("leftSpinner"));
        rightSpinner.setSelection(preferenceHelper.getInteger("rightSpinner"));
    }

        private void translate(final String word) {
        ApiService api = RetroClient.getApiService();
        final String leftSpinnerDir = LanguageConverter.getInstance().convert(getActivity(), leftSpinner.getSelectedItem().toString());
        final String rightSpinnerDir = LanguageConverter.getInstance().convert(getActivity(), rightSpinner.getSelectedItem().toString());
        if (!checkWordInDb(word, leftSpinnerDir+"-"+rightSpinnerDir)) {
                Call<Answer> call = api.getMyJSON(API_KEY, word, leftSpinnerDir + "-" + rightSpinnerDir);
                call.enqueue(new Callback<Answer>() {
                    @Override
                    public void onResponse(Call<Answer> call, Response<Answer> response) {
                        if (response.isSuccessful()) {
                            if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.translated_ok), Toast.LENGTH_SHORT).show();
                            insertWord(word, response.body().getText()[0], leftSpinnerDir + "-" + rightSpinnerDir);
                        } else {
                            //обработка ошибок
                            switch (response.code()){
                                case 400:
                                    if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_400), Toast.LENGTH_SHORT).show();
                                    break;
                                case 401:
                                    if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_401), Toast.LENGTH_SHORT).show();
                                    break;
                                case 402:
                                    if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_402), Toast.LENGTH_SHORT).show();
                                    break;
                                case 404:
                                    if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_404), Toast.LENGTH_SHORT).show();
                                    break;
                                case 413:
                                    if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_413), Toast.LENGTH_SHORT).show();
                                    break;
                                case 422:
                                    if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_422), Toast.LENGTH_SHORT).show();
                                    break;
                                case 501:
                                    if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_501), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Answer> call, Throwable t) {
                        if (isAdded()) Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.internet_issue), Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }

    //вставка перевода в БД и рассылка события о новом переводе по eventbus
    private void insertWord(String word, String translation, String dir) {
        SQLiteDatabase db = wordsDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WordsContract.WordEntry.COLUMN_WORD, word);
        values.put(WordsContract.WordEntry.COLUMN_TRANSLATED, translation);
        values.put(WordsContract.WordEntry.COLUMN_DIRECTION, dir);
        values.put(WordsContract.WordEntry.COLUMN_FAVOURITE, 0);
        long newRowId = db.insert(WordsContract.WordEntry.TABLE_NAME, null, values);
        EventBus.getDefault().post(new NewWordTranslated(word, translation, dir));
    }

    //проверка наличия перевода в базе данных
    private boolean checkWordInDb(String word, String dir) {
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
                WordsContract.WordEntry.COLUMN_WORD+"=? AND "+ WordsContract.WordEntry.COLUMN_DIRECTION+"=?",  // столбцы для условия WHERE
                new String[] { word, dir },    // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        if (cursor.moveToNext()) {
            int translatedColumnIndex = cursor.getColumnIndex(WordsContract.WordEntry.COLUMN_TRANSLATED);
            String currentTranslate = cursor.getString(translatedColumnIndex);
            txtTranslated.setText(currentTranslate);
            return true;
        }
        return false;
    }

    static class NewWordTranslated {
        String word;
        String translation;
        String dir;

        NewWordTranslated(String word, String translation, String dir) {
            this.word = word;
            this.translation = translation;
            this.dir = dir;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("translate", txtTranslated.getText());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        try {
            txtTranslated.setText(savedInstanceState.getCharSequence("translate"));
        } catch (NullPointerException e) {
            Log.d(getActivity().getLocalClassName(), "error while tried to restore instance state");
        }
    }

    @Subscribe()
    public void onNewWordTranslated(TranslatorFragment.NewWordTranslated event) {
        txtTranslated.setText(event.translation);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        preferenceHelper.putInteger("leftSpinner", leftSpinner.getSelectedItemPosition());
        preferenceHelper.putInteger("rightSpinner", rightSpinner.getSelectedItemPosition());
        EventBus.getDefault().unregister(this);
    }
}
