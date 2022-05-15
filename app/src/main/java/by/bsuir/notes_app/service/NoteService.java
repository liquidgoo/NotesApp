package by.bsuir.notes_app.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import by.bsuir.notes_app.NotesAppContext;
import by.bsuir.notes_app.model.Note;
import by.bsuir.notes_app.repository.NoteDbRepository;

import java.util.Collection;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class NoteService {

    private final NotesAppContext notesAppContext = NotesAppContext.getInstance();
    private NoteServiceListener listener;
    private List<Note> notes;
    private NoteDbRepository repository;

    public NoteService(Context context) {
        repository = notesAppContext.noteDbRepository(context);
    }

    public Note findById(int id) {
        for (Note n : notes) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    public Single<List<Note>> readAll() {
        return Single.fromCallable(() -> repository.readAll());
    }

    public Single<List<Note>> readByTitlePart(String titlePart) {
        return Single.fromCallable(() -> repository.readByTitlePart(titlePart));
    }

    public void replaceNotes(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }

    public Single<Boolean> add(Note note) {
        listener.onAdd();
        return Single.fromCallable(() -> {
            repository.save(note);
            return notes.add(note);
        });
    }

    public Single<Boolean> remove(@Nullable Object o) {
        listener.onRemove();
        return Single.fromCallable(() -> {
            repository.delete((Note) o);
            return notes.remove(o);
        });
    }

    public boolean addAll(@NonNull Collection<? extends Note> collection) {
        listener.onAdd();
        return notes.addAll(collection);
    }

    public Note get(int i) {
        return notes.get(i);
    }

    public Single<Note> set(Note note) {
        listener.onUpdate();
        return Single.fromCallable(() -> {
            int pos = getPosByNoteId(note.getId());
            if (pos == -1) {
                return null;
            }
            repository.update(note);
            notes.set(pos, note);
            return note;
        });
    }


    public Note remove(int i) {
        listener.onRemove();
        return notes.remove(i);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
         this.notes = notes;
    }

    public void setListener(NoteServiceListener listener) {
        this.listener = listener;
    }

    private int getPosByNoteId(int id) {
        for (int i = 0; i < notes.size(); i++) {
            Note n = notes.get(i);
            if (n.getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
