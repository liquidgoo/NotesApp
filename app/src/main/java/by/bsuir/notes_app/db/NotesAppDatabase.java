package by.bsuir.notes_app.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import by.bsuir.notes_app.model.Note;
import by.bsuir.notes_app.repository.NoteDbRepository;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesAppDatabase extends RoomDatabase {

    public abstract NoteDbRepository noteDbRepository();

}
