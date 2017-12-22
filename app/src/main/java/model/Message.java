package model;

/**
 * Created by Dell on 6/16/2017.
 */

public class Message {
    private int id;
    private String from;
    private String assignto;
    private String sessionid;
    private String timestamp;
    private String picture;
    private String departmentid;
    private String department;
    private String visitor_id;



    private String unseenmsg;
    private boolean isImportant;
    private boolean isRead;
    private int color = -1;



    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getAssignto() {
        return assignto;
    }

    public void setAssignto(String assignto) {
        this.assignto = assignto;
    }


    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }


    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }







    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUnseenmsg() {
        return unseenmsg;
    }

    public void setUnseenmsg(String unseenmsg) {
        this.unseenmsg = unseenmsg;
    }
}
