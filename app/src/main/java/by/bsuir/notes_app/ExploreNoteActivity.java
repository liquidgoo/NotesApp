package by.bsuir.notes_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import by.bsuir.notes_app.model.Note;

public class ExploreNoteActivity extends AppCompatActivity {

    private Note note;
    private TextView noteTitle;
    private TextView noteText;
    private TextView noteDate;

    private ActivityResultLauncher<Intent> editResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    Note note = data.getParcelableExtra("note");
                    noteTitle.setText(note.getTitle());
                    noteText.setText(note.getText());
                    this.note = note;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_note);

        noteTitle = findViewById(R.id.explore_note_title);
        noteText = findViewById(R.id.explore_note_text);
        noteDate = findViewById(R.id.explore_note_date);

        note = getIntent().getParcelableExtra("note");

        noteTitle.setText(note.getTitle());
        noteText.setText(note.getText());
        noteDate.setText(note.getCreateDate());
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickEdit(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        intent.putExtra("note", note);
        editResultLauncher.launch(intent);
    }

}