package by.bsuir.notes_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import by.bsuir.notes_app.databinding.ActivityCreateNoteBinding;
import by.bsuir.notes_app.model.Note;
import by.bsuir.notes_app.service.NoteService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateNoteActivity extends AppCompatActivity {

    private ActivityCreateNoteBinding binding;
    private NotesAppContext notesAppContext;
    private EditText createNoteTitleInput;
    private EditText createNoteTextInput;
    private boolean editing = false;
    private Note editingNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());

        notesAppContext = NotesAppContext.getInstance();

        createNoteTitleInput = findViewById(R.id.create_note_title_input);
        createNoteTextInput = findViewById(R.id.create_note_text_input);

        Intent intent = getIntent();
        if (intent.hasExtra("note")) {
            Note note = intent.getParcelableExtra("note");
            createNoteTitleInput.setText(note.getTitle());
            createNoteTextInput.setText(note.getText());
            editing = true;
            editingNote = note;
        }
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickSave(View view) {
        String noteTitle = createNoteTitleInput.getText().toString();
        String noteText = createNoteTextInput.getText().toString();
        NoteService service = notesAppContext.getNoteService(getApplicationContext());
        if (editing) {
            editingNote.setTitle(noteTitle);
            editingNote.setText(noteText);
            service.set(editingNote)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new SingleObserver<Note>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Note note) {
                            Intent intent = new Intent();
                            intent.putExtra("note", note);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(getApplicationContext(), "Failed to save note", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } else {
            Note note = new Note(noteTitle, noteText);
            service.add(note)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new SingleObserver<Boolean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@NonNull Boolean aBoolean) {
                            finish();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Toast.makeText(getApplicationContext(), "Failed to save note", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }
}