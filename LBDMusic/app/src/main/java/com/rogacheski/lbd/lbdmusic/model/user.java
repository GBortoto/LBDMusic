package com.rogacheski.lbd.lbdmusic.model;


import java.io.Serializable;

public class user implements Serializable{

    private String fantasyName;
    private String email;
    private String document;
    private String fname;
    private String lname;
    private String picture;
    private String backpicture;
    private String type;

    public String getFantasyName() { return fantasyName; }

    public String getFName() { return fname; }

    public String getLName() { return lname; }

    public String getPicture() { return picture; }

    public String getEmail() {
        return email;
    }

    public String getDocument() { return document; }

    public String getBackpicture() {
        return backpicture;
    }

    public String getType() { return type; }

    public void setFantasyName(String Sfname) { fantasyName = Sfname; }

    public void setFName(String Sfname) { fname = Sfname; }

    public void setLName(String Slname) { lname = Slname; }

    public void setPicture(String Spicture) { picture = Spicture; }

    public void setEmail(String Semail) { email = Semail; }

    public void setDocument(String Sdocument) { document = Sdocument; }

    public void setBackpicture(String Sbackpicture) { backpicture = Sbackpicture; }

    public void setType(String Stype) { type = Stype; }

}
