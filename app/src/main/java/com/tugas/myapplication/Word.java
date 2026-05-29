package com.tugas.myapplication;

public class Word {
    public int id;
    public String english;
    public String indonesia;

    public Word(int id, String english, String indonesia) {
        this.id = id;
        this.english = english;
        this.indonesia = indonesia;
    }

    public int getId() {
        return id;
    }

    public String getEnglish() {
        return english;
    }

    public String getIndonesia() {
        return indonesia;
    }
}
