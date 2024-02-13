package com.example.noteappication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  static final String PREFS_NAME ="NotePrefs";
    private  static final String KEY_NOTE_COUNT="NoteCount";
    private LinearLayout notesContainer;
    private List<Note> noteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesContainer=findViewById(R.id.notescontainer);
        Button saveButton =findViewById(R.id.savebutton);

        noteList = new ArrayList<>();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();

            }
        });
        loadNotesFromPreferences();
        displayNotes();


        }

    private void displayNotes() {
        for (Note note : noteList){
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT,0);

        for(int i=0; i<noteCount; i++){
            String title = sharedPreferences.getString("note_title" +i , "");
            String content = sharedPreferences.getString("note_content"+i,"");

            Note note = new Note();
            note.setTitle(title);
            note.setContext(content);
            noteList.add(note);

        }
    }

    private void saveNote() {
        EditText titleEditText =findViewById(R.id.titledittext);
        EditText contextEditText = findViewById(R.id.contectedittext);

        String title = titleEditText.getText().toString();
        String content = contextEditText.getText().toString();

        if(!title.isEmpty() && !content.isEmpty()){
            Note note = new Note();
            note.setTitle((title));
            note.setContext(content);

            noteList.add(note);
            saveNoteToPreference();


            createNoteView(note);
            clearInputFields();
        }
    }

    private void clearInputFields() {
        EditText titleEdittext = findViewById(R.id.titledittext);
        EditText contextEdittext = findViewById(R.id.contectedittext);
        titleEdittext.getText().clear();
        contextEdittext.getText().clear();

    }

    private void createNoteView(final Note note) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item,null);
        TextView titleTextView = noteView.findViewById(R.id.TText);
        TextView contentTextView = noteView.findViewById(R.id.ContentTextview);
     titleTextView.setText(note.getTitle());
     contentTextView.setText(note.getContext());


     noteView.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
             showDeleteDialog(note);
             return true;
         }
     });
     notesContainer.addView(noteView);
    }

    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete This Note");
        builder.setMessage("Are your sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNoteAndRefresh(note);
            }
        });
        builder.setNegativeButton("Cancle", null);
        builder.show();
    }

    private void deleteNoteAndRefresh(Note note) {
        noteList.remove(note);
        saveNoteToPreference();
        refreshNoteView();
    }

    private void refreshNoteView() {
        notesContainer.removeAllViews();
        displayNotes();
    }

    private void saveNoteToPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT,noteList.size());
        for(int i=0;i<noteList.size();i++){
            Note note= noteList.get(i);
            editor.putString("note_title_" + i, note.getTitle());
            editor.putString("note_content" + i, note.getContext());

        }
        editor.apply();
    }
}