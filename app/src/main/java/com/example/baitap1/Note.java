package com.example.baitap1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Note {
    private String title;
    private String content;
    private String timestamp;
    private int id;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        this.id = (int) System.currentTimeMillis();
    }

    public Note(int id, String title, String content, String timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
