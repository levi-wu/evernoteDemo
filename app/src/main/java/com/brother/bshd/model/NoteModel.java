package com.brother.bshd.model;

/**
 * Created by wule on 2017/04/12.
 */

public class NoteModel {
    private String noteGuid;
    private String title;
    private String date;

    public NoteModel(String date, String noteGuid, String title) {
        this.date = date;
        this.noteGuid = noteGuid;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoteGuid() {
        return noteGuid;
    }

    public void setNoteGuid(String noteGuid) {
        this.noteGuid = noteGuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
