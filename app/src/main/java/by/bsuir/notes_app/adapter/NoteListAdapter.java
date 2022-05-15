package by.bsuir.notes_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import by.bsuir.notes_app.databinding.NoteListItemBinding;
import by.bsuir.notes_app.model.Note;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteListItemViewHolder> {

    public static class NoteListItemViewHolder extends RecyclerView.ViewHolder {

        private final NoteListItemBinding binding;

        public NoteListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = NoteListItemBinding.bind(itemView);
        }
    }

    private final List<Note> notes;

    public NoteListAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NoteListItemBinding binding = NoteListItemBinding.inflate(inflater, parent, false);
        View view = binding.getRoot();

        return new NoteListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListItemViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.binding.noteTitle.setText(note.getTitle());
        holder.binding.noteDate.setText(note.getCreateDate());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

}
