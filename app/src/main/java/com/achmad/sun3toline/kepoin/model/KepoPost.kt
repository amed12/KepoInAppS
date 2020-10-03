/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 9:19 PM
 *
 */

package com.achmad.sun3toline.kepoin.model

/**
 * Created by coldwarrior on 23/11/16.
 */
class KepoPost {
    private var image: String? = null
    private var judul: String? = null
    private var penjelasan: String? = null
    private var provinsi: String? = null
    private var kategori: String? = null
    private var username: String? = null

    constructor()
    constructor(image: String?, judul: String?, penjelasan: String?, provinsi: String?, kategori: String?, username: String?) {
        this.image = image
        this.judul = judul
        this.penjelasan = penjelasan
        this.provinsi = provinsi
        this.kategori = kategori
        this.username = username
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getJudul(): String? {
        return judul
    }

    fun setJudul(judul: String?) {
        this.judul = judul
    }

    fun getPenjelasan(): String? {
        return penjelasan
    }

    fun setPenjelasan(penjelasan: String?) {
        this.penjelasan = penjelasan
    }

    fun getProvinsi(): String? {
        return provinsi
    }

    fun setProvinsi(provinsi: String?) {
        this.provinsi = provinsi
    }

    fun getKategori(): String? {
        return kategori
    }

    fun setKategori(kategori: String?) {
        this.kategori = kategori
    }

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }
}