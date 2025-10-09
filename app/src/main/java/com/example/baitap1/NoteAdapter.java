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
    private List<Note> noteList;
    private OnNoteClickListener listener;

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
        holder.titleTextView.setText(note.getTitle());
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

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, timestampTextView;
        Button editButton, deleteButton;

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
