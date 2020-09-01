package com.example.filelocker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements AddNoteDialog.AddNoteDialogListener {
    private ArrayList<String> notes;
    private ArrayAdapter<String> notesAdapter;
    private ListView listView;
    private aesUtils aes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.ListViewNotes);
        notes = new ArrayList<String>();
        notesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(notesAdapter);
        setupListViewListener();
        //#todo add notes from lines from the name txt file (read)
        BufferedReader input = null;
        String file = "Title.txt";

        try {
            input = new BufferedReader(
                    new InputStreamReader(openFileInput(file)));
            String line;
            StringBuffer buffer =new StringBuffer();
            while((line=input.readLine())!=null){
                //notes.add(line);
                notesAdapter.add(line);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }

        //notes.add("First Item");
        //notes.add("Second Item");
    }
    private void setupListViewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //call a new activity from here and pass the position
               startDisplayActivity(position);
            }
        });

    }
    private void startDisplayActivity(int position){
        Intent intent = new Intent(this,DisplayActivity.class);
        int pos= position;
        intent.putExtra("message",pos);
        startActivity(intent);
    }
    public void addNote(View v){
        AddNoteDialog addNoteDialog = new AddNoteDialog();
        addNoteDialog.show(getSupportFragmentManager(), "addNoteDialog");
        //notesAdapter.add("Note position = " +notes.size());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void applyTexts(String title, String note, String password) {
        //save these values to a file
        // #todo encrypt and save name to name file, note in new note file
        aes=new aesUtils();
        HashMap<String,String> cipherText = new HashMap<>();
        try {
            cipherText = aes.encrypt(password,note);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

      /*  try {
            FileOutputStream fileOutputStream = new FileOutputStream(filename);//openFileOutput(filename,MODE_PRIVATE);
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            //writer.write(note);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(cipherText);
            objectOutputStream.close();
            fileOutputStream.close();
            *//*writer.write(cipherText.get("message")+"\n");
            writer.write(cipherText.get("iv")+"\n");
            writer.write(cipherText.get("salt")+"\n");
            writer.close();*//*
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String saltFileName = "salt"+notes.size()+".txt";
        String saltFile = cipherText.get("salt");
        String ivFileName = "iv"+notes.size()+".txt";
        String ivFile = cipherText.get("iv");
        String messageFileName = "message"+notes.size()+".txt";
        String messageFile = cipherText.get("message");

        try {
            FileOutputStream fileOutputStream = openFileOutput(saltFileName,MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(saltFile);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         try {
            FileOutputStream fileOutputStream = openFileOutput(ivFileName,MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(ivFile);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         try {
            FileOutputStream fileOutputStream = openFileOutput(messageFileName,MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(messageFile);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileTitleName = "Title.txt";
        try {
            FileOutputStream fileOutputStream1 = openFileOutput(fileTitleName,MODE_APPEND);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream1));
            writer.write(title+"\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //notesAdapter.add(title + " " + note + notes.size());
        notes.add(title);
        //encrypt the note
    }
}