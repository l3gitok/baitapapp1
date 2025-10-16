package com.example.baitap1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteStorage {
    private static final String PREFS_NAME = "NotePrefs";
    private static final String NOTES_KEY = "notes";
    private static final String NOTE_DELIMITER = ";";
    private static final String FIELD_DELIMITER = "\\|"; // Properly escape delimiter for regex

    public static void saveNotes(Context context, List<Note> notes) {
        if (context == null || notes == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        StringBuilder data = new StringBuilder();
        for (Note note : notes) {
            data.append(note.getId()).append("|")
                .append(note.getTitle()).append("|")
                .append(note.getContent()).append("|")
                .append(note.getTimestamp()).append(";");
        }
        prefs.edit().putString(NOTES_KEY, data.toString()).apply();
    }

    public static List<Note> loadNotes(Context context) {
        List<Note> notes = new ArrayList<>();
        if (context == null) return notes;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String data = prefs.getString(NOTES_KEY, null);
        if (data == null || data.isEmpty()) return notes;

        String[] noteStrings = data.split(NOTE_DELIMITER);
        for (String noteString : noteStrings) {
            String[] fields = noteString.split(FIELD_DELIMITER);
            if (fields.length == 4) {
                try {
                    int id = Integer.parseInt(fields[0]);
                    String title = fields[1];
                    String content = fields[2];
                    String timestamp = fields[3];
                    notes.add(new Note(id, title, content, timestamp));
                } catch (NumberFormatException e) {
                    Log.e("NoteStorage", "Error parsing note ID", e); // Replace printStackTrace
                }
            }
        }
        return notes;
    }

    public static void clearNotes(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(NOTES_KEY).apply();
    }
}
