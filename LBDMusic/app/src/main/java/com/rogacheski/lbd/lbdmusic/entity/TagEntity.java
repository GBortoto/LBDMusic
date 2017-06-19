package com.rogacheski.lbd.lbdmusic.entity;

/**
 * Created by Vinicius_a on 28-Apr-17.
 */

public class TagEntity {
    private int id_tag;
    private String genre;

    public TagEntity(int id , String genre){
        this.genre = genre;
        id_tag = id;
    }

    public int getId_tag() {
        return id_tag;
    }

    public void setId_tag(int id_tag) {
        this.id_tag = id_tag;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}


