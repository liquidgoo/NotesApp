package by.bsuir.notes_app.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import by.bsuir.notes_app.model.Note;

import java.util.List;

@Dao
public interface NoteDbRepository {

    @Query("select * from Note")
    List<Note> readAll();

    @Query("select * from Note where id = :id")
    Note read(int id);

    @Query("select * from Note where title like '%' || :titlePart || '%'")
    List<Note> readByTitlePart(String titlePart);

    @Insert
    long save(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

}
