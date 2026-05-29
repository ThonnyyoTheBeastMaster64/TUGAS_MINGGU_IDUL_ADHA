package com.tugas.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<Word> wordList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Word word);
        void onDeleteClick(Word word);
    }

    public WordAdapter(List<Word> wordList, OnItemClickListener listener) {
        this.wordList = wordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.tvEnglish.setText(word.getEnglish());
        holder.tvIndonesia.setText(word.getIndonesia());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(word));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(word));
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void updateList(List<Word> newList) {
        this.wordList = newList;
        notifyDataSetChanged();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView tvEnglish, tvIndonesia;
        ImageButton btnEdit, btnDelete;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnglish = itemView.findViewById(R.id.tvEnglish);
            tvIndonesia = itemView.findViewById(R.id.tvIndonesia);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
