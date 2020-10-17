package com.example.filelocker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DisplayActivity extends AppCompatActivity {
    TextView title,content;
    private aesUtils aes;
    private HashMap map;
    private String message,iv,salt;   //message,iv,salt
    private EditText passwordEditText;
//    private Button decryptButton;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Intent intent = getIntent();
//        decryptButton= findViewById(R.id.DecryptButton);
        passwordEditText= (EditText)findViewById(R.id.inputPassword);
        title = (TextView) findViewById(R.id.TitleTextView);
        content = (TextView) findViewById(R.id.NoteTextView);
        int pos = intent.getIntExtra("message",420);
        position=pos;
        //title.setText("Title no"+pos);
        content = (TextView) findViewById(R.id.NoteTextView);
        content.setText("Enter password to decrypt");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    public void StartDecrypt(View v){
        String file = null;
          //  input = null;
        file = "file"+position+".ser";
        //passwordEditText= (EditText)findViewById(R.id.passwordText);
        String password=passwordEditText.getText().toString();

       /* try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            map = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            map.put("password",password);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            content.setText("Could not read file");
        }*/
       BufferedReader input1;
        try {
            input1 = new BufferedReader(
                    new InputStreamReader(openFileInput("salt"+position+".txt")));
            String line;
            StringBuffer buffer =new StringBuffer();
            while((line=input1.readLine())!=null){
                salt= line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } try {
            input1 = new BufferedReader(
                    new InputStreamReader(openFileInput("iv"+position+".txt")));
            String line;
            StringBuffer buffer =new StringBuffer();
            while((line=input1.readLine())!=null){
                iv= line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } try {
            input1 = new BufferedReader(
                    new InputStreamReader(openFileInput("message"+position+".txt")));
            String line;
            StringBuffer buffer =new StringBuffer();
            while((line=input1.readLine())!=null){
                message= line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        aes = new aesUtils();
        try {
            content.setText(aes.decryt(password,salt,iv,message));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            content.setText("Could not decrypt file...\nWrong password maybe...\nDont give up. Try again!");
        }

        BufferedReader input = null;
        file = "Title.txt";
        try {
            input = new BufferedReader(
                    new InputStreamReader(openFileInput(file)));
            String line = new String();
            StringBuffer buffer =new StringBuffer();
            for(int i =0;i<=position;i++) {
                line = input.readLine();
            }
            title.setText(line);
        } catch (IOException e) {
            e.printStackTrace();
            title.setText("Could not Find Title");
        }
    }
}