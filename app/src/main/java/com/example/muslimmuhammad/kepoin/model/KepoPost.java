package com.example.muslimmuhammad.kepoin.model;

/**
 * Created by coldwarrior on 23/11/16.
 */

public class KepoPost {
    private String image;
    private String judul;
    private String penjelasan;
    private String provinsi;
    private String kategori;
    private String username;
    public KepoPost(){

    }

    public KepoPost(String image, String judul, String penjelasan, String provinsi, String kategori, String username) {
        this.image = image;
        this.judul = judul;
        this.penjelasan = penjelasan;
        this.provinsi = provinsi;
        this.kategori = kategori;
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenjelasan() {
        return penjelasan;
    }

    public void setPenjelasan(String penjelasan) {
        this.penjelasan = penjelasan;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
