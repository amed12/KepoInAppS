/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 8:20 PM
 *
 */

package com.achmad.sun3toline.kepoin.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.achmad.sun3toline.kepoin.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_blog_single.*

class BlogSingleActivity : AppCompatActivity() {
    private var mpostKey: String = ""
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_single)
        mpostKey = intent.extras?.getString("blog_id").toString()
        mDatabase = FirebaseDatabase.getInstance().reference.child("Post")
        mAuth = FirebaseAuth.getInstance()
        mDatabase.child(mpostKey).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postTitle = dataSnapshot.child("judul").value as String?
                val postDesc = dataSnapshot.child("penjelasan").value as String?
                val postImage = dataSnapshot.child("image").value as String?
                val postUid = dataSnapshot.child("uid").value as String?
                val postUsername = dataSnapshot.child("username").value as String?
                val postKategori = dataSnapshot.child("kategori").value as String?
                val postProvinsi = dataSnapshot.child("provinsi").value as String?
                single_title.text = postTitle
                single_desc.text = postDesc
                Glide.with(this@BlogSingleActivity).load(postImage).into(single_image)
                single_kategori.text = postKategori
                single_provinsi.text = postProvinsi
                single_username.text = postUsername
                if (mAuth.currentUser?.uid.toString() == postUid) {
                    singleRemovebtn.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        singleRemovebtn.setOnClickListener {
            mDatabase.child(mpostKey).removeValue()
            val mainIntent = Intent(this@BlogSingleActivity, PostActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}