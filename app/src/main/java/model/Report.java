package model;

/**
 * Created by Dell on 7/4/2017.
 */

public class Report {
    public Report(){

    }

    private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContactnameId() {
        return ContactnameId;
    }

    public void setContactnameId(String contactnameId) {
        ContactnameId = contactnameId;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getNoteDate() {
        return NoteDate;
    }

    public void setNoteDate(String noteDate) {
        NoteDate = noteDate;
    }

    private String ContactnameId;
    private String Note;
    private String NoteDate;
}
