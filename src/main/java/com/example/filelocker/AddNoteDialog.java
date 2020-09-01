package com.example.filelocker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddNoteDialog extends AppCompatDialogFragment{
    private EditText editTextTitle,editTextNote,editTextPassword;
    private AddNoteDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("New Note")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close teh dialog
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = editTextTitle.getText().toString();
                        String note = editTextNote.getText().toString();
                        String password = editTextPassword.getText().toString();
                        listener.applyTexts(title,note,password);
                    }
                });
        editTextTitle = view.findViewById(R.id.titleText);
        editTextNote = view.findViewById(R.id.noteText);
        editTextPassword = view.findViewById(R.id.passwordText);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement AddNoteDialogListener");
        }
    }

    public interface AddNoteDialogListener{
        void applyTexts(String title,String note,String password);

    }
}
