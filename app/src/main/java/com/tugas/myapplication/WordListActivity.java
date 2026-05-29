package com.tugas.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity implements WordAdapter.OnItemClickListener {

    private RecyclerView rvWords;
    private TextView tvEmpty;
    private EditText etSearch;
    private DictionaryDbHelper dbHelper;
    private WordAdapter adapter;
    private List<Word> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        rvWords = findViewById(R.id.rvWords);
        tvEmpty = findViewById(R.id.tvEmpty);
        etSearch = findViewById(R.id.etSearch);
        dbHelper = new DictionaryDbHelper(this);

        rvWords.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WordAdapter(words, this);
        rvWords.setAdapter(adapter);

        loadWords("");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadWords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadWords(String query) {
        if (query.isEmpty()) {
            words = dbHelper.listAll();
        } else {
            words = dbHelper.searchWords(query);
        }
        
        adapter.updateList(words);
        tvEmpty.setVisibility(words.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEditClick(Word word) {
        Intent intent = new Intent(this, EditDataActivity.class);
        intent.putExtra("WORD_ID", word.getId());
        intent.putExtra("WORD_ENGLISH", word.getEnglish());
        intent.putExtra("WORD_INDONESIA", word.getIndonesia());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Word word) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete '" + word.getEnglish() + "'?")
            .setPositiveButton("Delete", (dialog, which) -> {
                dbHelper.deleteWord(word.getId());
                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                loadWords(etSearch.getText().toString());
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWords(etSearch.getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) dbHelper.close();
    }
}
