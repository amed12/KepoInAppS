package com.achmad.sun3toline.kepoin.model;

/**
 * Created by coldwarrior on 21/11/16.
 */

public class PostModel {
    String image,nama,penjelasan;

    public PostModel(String image, String nama, String penjelasan) {
        this.image = image;
        this.nama = nama;
        this.penjelasan = penjelasan;
    }
    public PostModel(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPenjelasan() {
        return penjelasan;
    }

    public void setPenjelasan(String penjelasan) {
        this.penjelasan = penjelasan;
    }
}
