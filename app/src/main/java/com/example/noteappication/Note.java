package com.example.noteappication;

public class Note {
    private String title;
    private String context;
    public Note()
    {

    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getContext(){
        return context;

    }

    public void setContext(String context) {
        this.context = context;
    }
    public Note(String title,String context){
        this.title=title;
        this.context=context;
    }
}
