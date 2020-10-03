/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 9:19 PM
 *
 */

package com.achmad.sun3toline.kepoin.model

/**
 * Created by coldwarrior on 21/11/16.
 */
class PostModel {
    private var image: String? = null
    private var nama: String? = null
    private var penjelasan: String? = null

    constructor(image: String?, nama: String?, penjelasan: String?) {
        this.image = image
        this.nama = nama
        this.penjelasan = penjelasan
    }

    constructor()

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getNama(): String? {
        return nama
    }

    fun setNama(nama: String?) {
        this.nama = nama
    }

    fun getPenjelasan(): String? {
        return penjelasan
    }

    fun setPenjelasan(penjelasan: String?) {
        this.penjelasan = penjelasan
    }
}