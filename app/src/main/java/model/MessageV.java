package model;

import android.graphics.Bitmap;

/**
 * Created by Dell on 6/19/2017.
 */

public class MessageV {
    private String fromName;
    private String message;
    private String department;
    private String file;
    private String mid;
    private String msg_sent_id;
    private String msg_sent_type;
    private String type;
    private String type_indicator;
    private String uid;
    private String usess;
    private String utype;
    private Bitmap sendImage;
    private boolean isSelf;










    public MessageV() {
    }



    public MessageV(String fromName, String message,String department,String file,String mid,String msg_sent_id,String msg_sent_type,
                   String type,String type_indicator,String uid,String usess,String utype, boolean isSelf, Bitmap sendImage ) {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
        this.sendImage = sendImage;
        this.department=department;
        this.file = file;
        this.mid =mid;
        this.msg_sent_id = msg_sent_id;
        this.msg_sent_type = msg_sent_type;
        this.type=type;
        this.type_indicator = type_indicator;
        this.uid=uid;
        this.usess=usess;
        this.utype =utype;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public Bitmap getSendImage() {
        return sendImage;
    }

    public void setSendImage(Bitmap sendImage) {
        this.sendImage = sendImage;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMsg_sent_id() {
        return msg_sent_id;
    }

    public void setMsg_sent_id(String msg_sent_id) {
        this.msg_sent_id = msg_sent_id;
    }

    public String getMsg_sent_type() {
        return msg_sent_type;
    }

    public void setMsg_sent_type(String msg_sent_type) {
        this.msg_sent_type = msg_sent_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_indicator() {
        return type_indicator;
    }

    public void setType_indicator(String type_indicator) {
        this.type_indicator = type_indicator;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsess() {
        return usess;
    }

    public void setUsess(String usess) {
        this.usess = usess;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }


}
