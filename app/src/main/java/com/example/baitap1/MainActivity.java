package com.example.baitap1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private FloatingActionButton fabAddNote;
    private TextView emptyStateText;

    private ActivityResultLauncher<Intent> addEditNoteLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupActivityResultLauncher();
        setupRecyclerView();
        setupFab();
        updateEmptyState();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAddNote = findViewById(R.id.fabAddNote);
        emptyStateText = findViewById(R.id.emptyStateText);
        noteList = new ArrayList<>();
    }

    private void setupActivityResultLauncher() {
        addEditNoteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String title = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_TITLE);
                        String content = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_CONTENT);
                        boolean isEditMode = data.getBooleanExtra(AddEditNoteActivity.EXTRA_IS_EDIT_MODE, false);

                        if (isEditMode) {
                            // Cập nhật ghi chú
                            int position = data.getIntExtra(AddEditNoteActivity.EXTRA_POSITION, -1);
                            int noteId = data.getIntExtra(AddEditNoteActivity.EXTRA_NOTE_ID, -1);
                            String timestamp = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_TIMESTAMP);

                            if (position >= 0 && position < noteList.size()) {
                                Note updatedNote = new Note(noteId, title, content, timestamp);
                                noteAdapter.updateNote(position, updatedNote);
                                Toast.makeText(this, "Ghi chú đã được cập nhật", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Thêm ghi chú mới
                            Note newNote = new Note(title, content);
                            noteAdapter.addNote(newNote);
                            Toast.makeText(this, "Ghi chú đã được thêm", Toast.LENGTH_SHORT).show();
                        }

                        updateEmptyState();
                    }
                }
        );
    }

    private void setupRecyclerView() {
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);
    }

    private void setupFab() {
        fabAddNote.setOnClickListener(v -> openAddNoteActivity());
    }

    private void updateEmptyState() {
        if (noteList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
        }
    }

    private void openAddNoteActivity() {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_IS_EDIT_MODE, false);
        addEditNoteLauncher.launch(intent);
    }

    private void openEditNoteActivity(Note note, int position) {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        intent.putExtra(AddEditNoteActivity.EXTRA_IS_EDIT_MODE, true);
        intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.getId());
        intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_TITLE, note.getTitle());
        intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_CONTENT, note.getContent());
        intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_TIMESTAMP, note.getTimestamp());
        intent.putExtra(AddEditNoteActivity.EXTRA_POSITION, position);
        addEditNoteLauncher.launch(intent);
    }

    @Override
    public void onEditClick(Note note, int position) {
        openEditNoteActivity(note, position);
    }

    @Override
    public void onDeleteClick(Note note, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Ghi chú")
                .setMessage("Bạn có chắc chắn muốn xóa ghi chú \"" + note.getTitle() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    noteAdapter.removeNote(position);
                    updateEmptyState();
                    Toast.makeText(this, "Ghi chú đã được xóa", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}