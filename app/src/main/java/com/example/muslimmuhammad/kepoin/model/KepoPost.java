package com.example.muslimmuhammad.kepoin.model;

/**
 * Created by coldwarrior on 23/11/16.
 */

public class KepoPost {
    private String image;
    private String judul;
    private String penjelasan;

    public KepoPost(){

    }
    public KepoPost(String image, String judul, String penjelasan) {
        this.image = image;
        this.judul = judul;
        this.penjelasan = penjelasan;
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
}
