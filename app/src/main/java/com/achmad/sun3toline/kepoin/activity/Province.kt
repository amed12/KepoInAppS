/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 8:35 PM
 *
 */

package com.achmad.sun3toline.kepoin.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.achmad.sun3toline.kepoin.R

class Province : AppCompatActivity() {
    var context: Context? = null

    //    //Membuat data
    private var arrayProvince = arrayOf("Aceh", "Bali", "Bangka Belitung", "Banten", "Bengkulu", "Gorontalo", "Jakarta", "Jambi", "Jawa Barat", "Jawa Tengah", "Jawa Timur", "Kalimantan Barat", "Kalimantan Selatan", "Kalimantan Tengah", "kalimantan Timur", "Kepuluan Riau", "Lampung", "Maluku", "Maluku Utara", "Nusa Tenggara Barat", "Nusa Tenggara Timur", "Papua", "Papua Barat", "Riau", "Sulawesi Barat", "Sulawesi Selatan", "Sulawesi Tengah", "Sulawesi Tenggara", "Sumatera Utara", "Sumatera Barat", "Sumatera Selatan", "Yogyakarta"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_province)
        context = this@Province

        //Inisialisasi ListView
        val listView = findViewById<ListView?>(R.id.listView)
        val jupuk1 = findViewById<TextView?>(R.id.jupuk)
        jupuk1.text = intent.getStringExtra("nama")
        //Membuat Adapter
        val arrayAdapter = ArrayAdapter(this,
                R.layout.item_province, arrayProvince)

        //Mengatur adapter
        listView.adapter = arrayAdapter

        //Memberi aksi ketika di klik masing2 item
        listView.setOnItemClickListener { _, _, position,
                                          _ ->

            //Pindah activity dan mengirim string 'data' di extra
            val intent = Intent(context, Post::class.java)
            intent.putExtra("data1", arrayProvince[position])
            intent.putExtra("data2", jupuk1.text.toString())
            startActivity(intent)
        }
    }
}