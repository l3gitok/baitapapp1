package com.example.baitap1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final List<Note> noteList;
    private final OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onEditClick(Note note, int position);
        void onDeleteClick(Note note, int position);
    }

    public NoteAdapter(List<Note> noteList, OnNoteClickListener listener) {
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        boolean isTitleEmpty = note.getTitle() == null || note.getTitle().trim().isEmpty();
        boolean isContentNotEmpty = note.getContent() != null && !note.getContent().trim().isEmpty();
        if (isTitleEmpty && isContentNotEmpty) {
            holder.titleTextView.setText("Không có tiêu đề");
            holder.titleTextView.setTextColor(holder.titleTextView.getResources().getColor(android.R.color.darker_gray));
            holder.titleTextView.setTypeface(holder.titleTextView.getTypeface(), android.graphics.Typeface.ITALIC);
        } else {
            holder.titleTextView.setText(note.getTitle());
            holder.titleTextView.setTextColor(holder.titleTextView.getResources().getColor(android.R.color.black));
            holder.titleTextView.setTypeface(holder.titleTextView.getTypeface(), android.graphics.Typeface.NORMAL);
        }
        holder.contentTextView.setText(note.getContent());
        holder.timestampTextView.setText(note.getTimestamp());

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(note, position);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(note, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateNote(int position, Note note) {
        noteList.set(position, note);
        notifyItemChanged(position);
    }

    public void removeNote(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void addNote(Note note) {
        noteList.add(0, note);
        notifyItemInserted(0);
    }

    public void setNotes(List<Note> notes) {
        noteList.clear();
        noteList.addAll(notes);
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTextView;
        final TextView contentTextView;
        final TextView timestampTextView;
        final Button editButton;
        final Button deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
