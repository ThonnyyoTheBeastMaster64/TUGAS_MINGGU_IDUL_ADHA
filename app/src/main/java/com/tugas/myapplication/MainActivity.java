package com.tugas.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etEnglish, etIndonesia;
    private TextView tvMessage;
    private DictionaryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEnglish = findViewById(R.id.etEnglish);
        etIndonesia = findViewById(R.id.etIndonesia);
        tvMessage = findViewById(R.id.tvMessage);

        dbHelper = new DictionaryDbHelper(this);
    }

    public void translate(View view) {
        String englishWord = etEnglish.getText().toString().trim();

        if (englishWord.isEmpty()) {
            tvMessage.setText("Please enter an English word!");
            tvMessage.setVisibility(View.VISIBLE);
            etIndonesia.setText("");
            return;
        }

        String translation = dbHelper.queryTranslation(englishWord);

        if (translation != null) {
            etIndonesia.setText(translation);
            tvMessage.setVisibility(View.GONE);
        } else {
            etIndonesia.setText("");
            tvMessage.setText("Word not found in dictionary.");
            tvMessage.setVisibility(View.VISIBLE);
        }
    }

    public void addData(View view) {
        Intent intent = new Intent(MainActivity.this, AddDataActivity.class);
        startActivity(intent);
    }

    public void viewAllData(View view) {
        Intent intent = new Intent(MainActivity.this, WordListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}