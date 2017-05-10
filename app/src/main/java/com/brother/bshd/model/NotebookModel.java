package com.brother.bshd.model;

/**
 * Created by wule on 2017/04/11.
 */

public class NotebookModel {
    private String notebookGuid;
    private String notebookName;
    private String noteNum;

    public NotebookModel(String notebookGuid, String notebookName, String noteNum) {
        this.notebookGuid = notebookGuid;
        this.notebookName = notebookName;
        this.noteNum = noteNum;
    }

    public String getNotebookGuid() {
        return notebookGuid;
    }

    public void setNotebookGuid(String notebookGuid) {
        this.notebookGuid = notebookGuid;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }

    public String getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(String noteNum) {
        this.noteNum = noteNum;
    }

}
