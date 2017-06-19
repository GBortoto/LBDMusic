package com.rogacheski.lbd.lbdmusic.entity;

import android.graphics.drawable.Drawable;

import java.util.Comparator;

/**
 * Created by vis_a on 28-Apr-17.
 */

public class ContatoEntity {
    int iTypeContact;
    String description;
    String sValue;

    public ContatoEntity(int iTypeContact, String description, String sValue) {
        this.iTypeContact = iTypeContact;
        this.description = description;
        this.sValue = sValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getiTypeContact() {
        return iTypeContact;
    }

    public void setiTypeContact(int iTypeContact) {
        this.iTypeContact = iTypeContact;
    }

    public String getsValue() {
        return sValue;
    }

    public void setsValue(String sValue) {
        this.sValue = sValue;
    }


}
