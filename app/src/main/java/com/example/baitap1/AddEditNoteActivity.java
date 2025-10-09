package com.example.baitap1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "note_id";
    public static final String EXTRA_NOTE_TITLE = "note_title";
    public static final String EXTRA_NOTE_CONTENT = "note_content";
    public static final String EXTRA_NOTE_TIMESTAMP = "note_timestamp";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_IS_EDIT_MODE = "is_edit_mode";

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContent;
    private Button buttonSave;
    private Toolbar toolbar;

    private boolean isEditMode = false;
    private int noteId = -1;
    private int position = -1;
    private String originalTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        initViews();
        setupToolbar();
        getIntentData();
        setupSaveButton();
        setupBackPressedCallback();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> handleBackPress());
    }

    private void getIntentData() {
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra(EXTRA_IS_EDIT_MODE, false);

        if (isEditMode) {
            // Chế độ chỉnh sửa
            noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1);
            position = intent.getIntExtra(EXTRA_POSITION, -1);
            originalTimestamp = intent.getStringExtra(EXTRA_NOTE_TIMESTAMP);

            String title = intent.getStringExtra(EXTRA_NOTE_TITLE);
            String content = intent.getStringExtra(EXTRA_NOTE_CONTENT);

            if (title != null) editTextTitle.setText(title);
            if (content != null) editTextContent.setText(content);

            setTitle("Chỉnh sửa Ghi chú");
            buttonSave.setText("Cập nhật");
        } else {
            // Chế độ thêm mới
            setTitle("Thêm Ghi chú Mới");
            buttonSave.setText("Lưu");
        }
    }

    private void setupSaveButton() {
        buttonSave.setOnClickListener(v -> saveNote());
    }

    private void setupBackPressedCallback() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        });
    }

    private void saveNote() {
        String title = editTextTitle.getText() != null ? editTextTitle.getText().toString().trim() : "";
        String content = editTextContent.getText() != null ? editTextContent.getText().toString().trim() : "";

        if (title.isEmpty()) {
            editTextTitle.setError("Vui lòng nhập tiêu đề");
            editTextTitle.requestFocus();
            return;
        }

        if (content.isEmpty()) {
            editTextContent.setError("Vui lòng nhập nội dung");
            editTextContent.requestFocus();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_NOTE_TITLE, title);
        resultIntent.putExtra(EXTRA_NOTE_CONTENT, content);
        resultIntent.putExtra(EXTRA_IS_EDIT_MODE, isEditMode);

        if (isEditMode) {
            resultIntent.putExtra(EXTRA_NOTE_ID, noteId);
            resultIntent.putExtra(EXTRA_POSITION, position);
            resultIntent.putExtra(EXTRA_NOTE_TIMESTAMP, originalTimestamp);
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void handleBackPress() {
        // Kiểm tra xem có thay đổi nào không
        String currentTitle = editTextTitle.getText() != null ? editTextTitle.getText().toString().trim() : "";
        String currentContent = editTextContent.getText() != null ? editTextContent.getText().toString().trim() : "";

        if (!currentTitle.isEmpty() || !currentContent.isEmpty()) {
            // Có nội dung, hỏi xác nhận
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Thoát không lưu?")
                    .setMessage("Bạn có những thay đổi chưa được lưu. Bạn có chắc muốn thoát?")
                    .setPositiveButton("Thoát", (dialog, which) -> {
                        setResult(RESULT_CANCELED);
                        finish();
                    })
                    .setNegativeButton("Ở lại", null)
                    .show();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
