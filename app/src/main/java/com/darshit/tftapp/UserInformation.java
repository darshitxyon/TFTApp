package com.darshit.tftapp;

/**
 * Created by MAHE on 4/7/2018.
 */

public class UserInformation {
    private String TutorName;
    private String FileLink;
    private String filename;
    private String College;
    private String course;
    private String userID;
    public UserInformation(){

    }

    public String getTutorName() {
        return TutorName;
    }

    public void setTutorName(String tutorName) {
        TutorName = tutorName;
    }

    public String getFileLink() {
        return FileLink;
    }

    public void setFileLink(String fileLink) {
        FileLink = fileLink;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
