package by.bsuir.notes_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


import by.bsuir.notes_app.adapter.NoteListAdapter;
import by.bsuir.notes_app.databinding.ActivityMainBinding;
import by.bsuir.notes_app.listener.RecyclerItemClickListener;
import by.bsuir.notes_app.model.Note;
import by.bsuir.notes_app.service.NoteService;
import by.bsuir.notes_app.service.NoteServiceListener;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NoteListAdapter adapter;
    private NotesAppContext notesAppContext;
    private NoteService noteService;
    private EditText searchEditText;
    private ImageButton searchCancelButton;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        searchEditText = findViewById(R.id.main_search_name_input);
        searchCancelButton = findViewById(R.id.main_search_cancel_btn);
        searchButton = findViewById(R.id.main_search_btn);

        notesAppContext = NotesAppContext.getInstance();
        noteService = notesAppContext.getNoteService(getApplicationContext());
        notesAppContext.setListener(noteServiceListener);
        RecyclerView notesList = findViewById(R.id.notes_list);
        noteService.readAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<List<Note>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Note> notes) {
                        noteService.setNotes(notes);
                        adapter = new NoteListAdapter(noteService.getNotes());
                        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                                StaggeredGridLayoutManager.VERTICAL);
                        notesList.setLayoutManager(layoutManager);
                        notesList.setAdapter(adapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

        notesList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), notesList,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {
                                Note selected = noteService.get(position);
                                Intent intent = new Intent(MainActivity.this, ExploreNoteActivity.class);
                                intent.putExtra("note", selected);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                        }));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = searchEditText.getText().toString();
                if (text.trim().replaceAll("\\n", "")
                        .isEmpty()) {
                    noteService.readAll()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new SingleObserver<List<Note>>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull List<Note> notes) {
                                    noteService.replaceNotes(notes);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }
                            });
                    return;
                }
                noteService.readByTitlePart(text)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new SingleObserver<List<Note>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull List<Note> notes) {
                                noteService.replaceNotes(notes);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onClickCreateNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);
    }

    private NoteServiceListener noteServiceListener = new NoteServiceListener() {

        @Override
        public void onAdd() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onUpdate() {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemove() {
            adapter.notifyDataSetChanged();
        }
    };

    public void onClickSearch(View view) {
        searchEditText.setVisibility(View.VISIBLE);
        searchCancelButton.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
    }

    public void onClickCancelSearch(View view) {
        searchEditText.setVisibility(View.INVISIBLE);
        searchCancelButton.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.VISIBLE);
    }
}