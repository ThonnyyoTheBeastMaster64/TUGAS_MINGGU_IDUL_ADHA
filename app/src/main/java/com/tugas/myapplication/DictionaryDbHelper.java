package com.tugas.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DictionaryDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dictionary.db";
    // Increment version to force onUpgrade and create the missing table
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "dictionary";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ENGLISH = "english";
    public static final String COLUMN_INDONESIA = "indonesia";

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ENGLISH + " TEXT NOT NULL UNIQUE, "
            + COLUMN_INDONESIA + " TEXT NOT NULL" + ");";

    public DictionaryDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables to start fresh
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS words"); 
        onCreate(db);
    }

    public long addEntry(String english, String indonesia) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ENGLISH, english);
        cv.put(COLUMN_INDONESIA, indonesia);
        return db.insert(TABLE_NAME, null, cv);
    }

    public String queryTranslation(String english) {
        SQLiteDatabase db = getReadableDatabase();
        String result = null;
        try (Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_INDONESIA},
                COLUMN_ENGLISH + " = ?", new String[]{english}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INDONESIA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Word> listAll() {
        List<Word> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ENGLISH + " ASC")) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String eng = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENGLISH));
                    String indo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INDONESIA));
                    list.add(new Word(id, eng, indo));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int updateEntry(int id, String english, String indonesia) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ENGLISH, english);
        cv.put(COLUMN_INDONESIA, indonesia);
        return db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Word> searchWords(String keyword) {
        List<Word> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_ENGLISH + " LIKE ? OR " + COLUMN_INDONESIA + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + keyword + "%", "%" + keyword + "%"};
        
        try (Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, COLUMN_ENGLISH + " ASC")) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String eng = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENGLISH));
                    String indo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INDONESIA));
                    list.add(new Word(id, eng, indo));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteWord(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
