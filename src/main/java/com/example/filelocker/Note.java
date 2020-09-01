package com.example.filelocker;

public class Note {
    private String name;
    private String contents;

    Note(String n, String c){
        name = n;
        contents = c;
    }

    public String getContents() {
        return contents;
    }

    public String getName() {
        return name;
    }
}
