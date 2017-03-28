package com.v1sar.yandextranslator.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.v1sar.yandextranslator.R;
import com.v1sar.yandextranslator.TranslatedWord;

import java.util.List;

/**
 * Created by qwerty on 28.03.17.
 */

public class TranslatedWordsAdapter extends RecyclerView.Adapter<TranslatedWordsAdapter.WordViewHolder> {

    private List<TranslatedWord> wordsList;

    class WordViewHolder extends RecyclerView.ViewHolder {
        private TextView toTranslate, translated, direction;

        WordViewHolder(View view) {
            super(view);
            toTranslate = (TextView) view.findViewById(R.id.word_to_translate);
            translated = (TextView) view.findViewById(R.id.translated_word);
            direction = (TextView) view.findViewById(R.id.translate_dir);
        }
    }

    public TranslatedWordsAdapter(List<TranslatedWord> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public TranslatedWordsAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.translated_row, parent, false);

        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TranslatedWordsAdapter.WordViewHolder holder, int position) {
        TranslatedWord word = wordsList.get(position);
        holder.toTranslate.setText(word.getWordToTranslate());
        holder.translated.setText(word.getTranslatedWord());
        holder.direction.setText(word.getTranslateDirection());
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }


}
