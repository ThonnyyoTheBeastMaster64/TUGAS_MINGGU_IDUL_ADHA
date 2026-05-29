package com.tugas.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AddDataActivity extends AppCompatActivity {

    private EditText etEnglish, etIndonesia;
    private TextView tvAddMessage;
    private DictionaryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        etEnglish = findViewById(R.id.etEnglish);
        etIndonesia = findViewById(R.id.etIndonesia);
        tvAddMessage = findViewById(R.id.tvAddMessage);
        dbHelper = new DictionaryDbHelper(this);
    }

    public void saveData(View view) {
        String english = etEnglish.getText().toString().trim();
        String indonesia = etIndonesia.getText().toString().trim();

        if (english.isEmpty() || indonesia.isEmpty()) {
            tvAddMessage.setText("Fields cannot be empty!");
            tvAddMessage.setTextColor(android.graphics.Color.RED);
            tvAddMessage.setVisibility(View.VISIBLE);
            return;
        }

        long id = dbHelper.addEntry(english, indonesia);

        if (id == -1) {
            tvAddMessage.setText("Failed to save. Word might already exist.");
            tvAddMessage.setTextColor(android.graphics.Color.RED);
            tvAddMessage.setVisibility(View.VISIBLE);
        } else {
            tvAddMessage.setText("Saved successfully!");
            tvAddMessage.setTextColor(android.graphics.Color.parseColor("#2E7D32"));
            tvAddMessage.setVisibility(View.VISIBLE);

            tvAddMessage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}