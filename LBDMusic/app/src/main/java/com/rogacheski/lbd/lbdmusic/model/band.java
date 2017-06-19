package com.rogacheski.lbd.lbdmusic.model;

import java.io.Serializable;

/**
 * Created by GBortoto on 17/06/2017.
 */

public class band extends user implements Serializable{
    /**
     * private String fantasyName;
     * private String email;
     * private String document;
     * private String fname;
     * private String lname;
     * private String picture;
     * private String backpicture;
     * private String type;
     */
    private int idUsuario;
    private int idAdress;
    private int nMembers;
    private String description;
    private String profilePicture;

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int id) {
        this.idUsuario = id;
    }

    public int getIdAdress() {
        return idAdress;
    }
    public void setIdAdress(int id) {
        this.idAdress = id;
    }

    public int getNMembers() {
        return nMembers;
    }
    public void setNMembers(int id) {
        this.idAdress = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}