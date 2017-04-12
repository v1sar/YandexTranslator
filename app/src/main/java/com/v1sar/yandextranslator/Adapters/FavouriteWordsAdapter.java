package com.v1sar.yandextranslator.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.v1sar.yandextranslator.Data.WordsContract;
import com.v1sar.yandextranslator.Data.WordsDbHelper;
import com.v1sar.yandextranslator.Helpers.TranslatedWord;
import com.v1sar.yandextranslator.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by qwerty on 12.04.17.
 */

public class FavouriteWordsAdapter extends RecyclerView.Adapter<FavouriteWordsAdapter.WordViewHolder>{
    private List<TranslatedWord> wordsList;
    private Context context;

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView toTranslate, translated, direction;
        private CheckBox isFav;
        private WordsDbHelper wordsDbHelper;

        WordViewHolder(View view) {
            super(view);
            toTranslate = (TextView) view.findViewById(R.id.word_to_translate);
            translated = (TextView) view.findViewById(R.id.translated_word);
            direction = (TextView) view.findViewById(R.id.translate_dir);
            isFav = (CheckBox) view.findViewById(R.id.favourite_word);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            isFav.setChecked(!isFav.isChecked());
            TranslatedWord tempWord = wordsList.get(getAdapterPosition());
            tempWord.setFavourite(isFav.isChecked());
            wordsList.set(getAdapterPosition(), tempWord);
            wordsDbHelper = WordsDbHelper.getInstance(context);
            SQLiteDatabase db = wordsDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(WordsContract.WordEntry.COLUMN_FAVOURITE, tempWord.favToInt());
            db.update(WordsContract.WordEntry.TABLE_NAME,
                    values,
                    "word = ? AND translated = ? AND direction = ?",
                    new String[] {tempWord.getWordToTranslate(), tempWord.getTranslatedWord(), tempWord.getTranslateDirection()});
            EventBus.getDefault().post(new NewWordUnFavourite(tempWord.getWordToTranslate(), tempWord.getTranslatedWord(), tempWord.getTranslateDirection()));
        }
    }

    public FavouriteWordsAdapter(List<TranslatedWord> wordsList, Context context) {
        this.wordsList = wordsList;
        this.context = context;
    }

    @Override
    public FavouriteWordsAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.translated_row, parent, false);
        return new FavouriteWordsAdapter.WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouriteWordsAdapter.WordViewHolder holder, int position) {
        final TranslatedWord word = wordsList.get(position);
        holder.toTranslate.setText(word.getWordToTranslate());
        holder.translated.setText(word.getTranslatedWord());
        holder.direction.setText(word.getTranslateDirection());
        holder.isFav.setChecked(word.isFavSelected());
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    public static class NewWordUnFavourite {
        public String word;
        public String translation;
        public String dir;

        NewWordUnFavourite(String word, String translation, String dir) {
            this.word = word;
            this.translation = translation;
            this.dir = dir;
        }
    }
}
