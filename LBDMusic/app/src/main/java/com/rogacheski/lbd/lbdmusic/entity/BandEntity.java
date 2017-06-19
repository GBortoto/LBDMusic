package com.rogacheski.lbd.lbdmusic.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BandEntity implements Serializable{

    /** idUsuario - ID da banda*/
    private int idUsuario;
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /** sNomeBanda - Nome da banda*/
    private String sNomeBanda;
    public String getsNomeBanda() {
        return sNomeBanda;
    }
    public void setsNomeBanda(String sNomeBanda) {
        this.sNomeBanda = sNomeBanda;
    }

    /** idAddress - Endereço da banda*/
    private int idAddress;
    public int getIdAddress() {
        return idAddress;
    }
    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }

    /** email - Email da banda*/
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /** fname - Primeiro nome do responsável da banda*/
    private String fname;
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    /** lname - Último nome do responsável da banda*/
    private String lname;
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }

    /** dImagemBanda - Imagem de capa da banda*/
    private String  dImagemBanda;
    public String getdImagemBanda() {
        return dImagemBanda;
    }
    public void setdImagemBanda(String dImagemBanda) {
        this.dImagemBanda = dImagemBanda;
    }

    /** dImagemDescBanda - Imagem da descrição da banda*/
    private String  dImagemDescBanda;
    public String getdImagemDescBanda() {
        return dImagemDescBanda;
    }
    public void setdImagemDescBanda(String dImagemDescBanda) {
        this.dImagemDescBanda = dImagemDescBanda;
    }

    /** tags - Tags de gênero da banda*/
    private List<TagEntity> tags;
    public List<TagEntity> getTags() {
        return tags;
    }
    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    /** listaContatos - Lista de contatos com a banda (Facebook, Youtube, etc.)*/
    private List<ContatoEntity> listaContatos;
    public List<ContatoEntity> getListaContatos() {
        return listaContatos;
    }
    public void setListaContatos(List<ContatoEntity> listaContatos) {
        this.listaContatos = listaContatos;
    }

    /** sDescricaoBanda - Texto de descrição da banda*/
    private String sDescricaoBanda;
    public String getsDescricaoBanda() {
        return sDescricaoBanda;
    }
    public void setsDescricaoBanda(String sDescricaoBanda) {
        this.sDescricaoBanda = sDescricaoBanda;
    }

    /** listaReviews - Lista de reviews da banda*/
    private List<ReviewsEntity> listaReviews;
    public List<ReviewsEntity> getListaReviews() {
        return listaReviews;
    }
    public void setListaReviews(List<ReviewsEntity> listaReviews) {
        this.listaReviews = listaReviews;
    }

    /** averageRating - Média de avaliações da banda*/
    private double averageRating;
    public double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
