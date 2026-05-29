package com.tugas.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditDataActivity extends AppCompatActivity {

    private EditText etEnglish, etIndonesia;
    private Button btnUpdate, btnBack;
    private DictionaryDbHelper dbHelper;
    private int wordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        etEnglish = findViewById(R.id.etEditEnglish);
        etIndonesia = findViewById(R.id.etEditIndonesia);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnEditBack);
        dbHelper = new DictionaryDbHelper(this);

        // Get data from intent
        wordId = getIntent().getIntExtra("WORD_ID", -1);
        etEnglish.setText(getIntent().getStringExtra("WORD_ENGLISH"));
        etIndonesia.setText(getIntent().getStringExtra("WORD_INDONESIA"));

        btnUpdate.setOnClickListener(v -> {
            String english = etEnglish.getText().toString().trim();
            String indonesia = etIndonesia.getText().toString().trim();

            if (english.isEmpty() || indonesia.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            int rowsAffected = dbHelper.updateEntry(wordId, english, indonesia);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) dbHelper.close();
    }
}
