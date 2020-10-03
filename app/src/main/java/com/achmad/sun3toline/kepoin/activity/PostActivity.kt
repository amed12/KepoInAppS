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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.achmad.sun3toline.kepoin.R
import com.achmad.sun3toline.kepoin.model.KepoPost
import com.achmad.sun3toline.kepoin.utils.extension.showToast
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.skydoves.whatif.whatIfNotNull
import kotlinx.android.synthetic.main.item_list.view.*

class PostActivity : AppCompatActivity() {
    private var mPostList: RecyclerView? = null
    private lateinit var mDatabase: Query
    var mProcessLike = false
    private var mDatabaseLike: DatabaseReference? = null
    private var mDatabaseCurrentUser: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kepo__post)
        mAuth = FirebaseAuth.getInstance()
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().reference.child("Post")
        mDatabaseLike = FirebaseDatabase.getInstance().reference.child("Likes")
        mDatabase = FirebaseDatabase.getInstance().reference.child("Post")
        mDatabaseLike?.keepSynced(true)
        mDatabase.keepSynced(true)
        mDatabaseCurrentUser?.keepSynced(true)
        mPostList = findViewById(R.id.post_list)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        mPostList?.setHasFixedSize(true)
        mPostList?.layoutManager = layoutManager
    }

    override fun onStart() {
        super.onStart()
        loadPhoto()
    }

    private fun loadPhoto() {
        showProgress()
        val options: FirebaseRecyclerOptions<KepoPost> = FirebaseRecyclerOptions.Builder<KepoPost>()
                .setQuery(mDatabase, KepoPost::class.java)
                .build()
        val adapter: FirebaseRecyclerAdapter<KepoPost, BlogViewHolder> = object : FirebaseRecyclerAdapter<KepoPost, BlogViewHolder>(
                options
        ) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
                return BlogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
            }

            override fun onBindViewHolder(viewHolder: BlogViewHolder, position: Int, model: KepoPost) {
                val postKey: String? = getRef(position).key
                postKey.whatIfNotNull(
                        whatIf = {
                            viewHolder.bind(model, it)
                            viewHolder.itemView.setOnClickListener {
                                val singleBlogIntent = Intent(this@PostActivity, BlogSingleActivity::class.java)
                                singleBlogIntent.putExtra("blog_id", postKey)
                                startActivity(singleBlogIntent)
                            }
                        },
                        whatIfNot = {
                            showToast(R.string.error_take_image)
                        }
                )
            }
        }
        mPostList?.adapter = adapter
        closeProgress()
    }

    private fun closeProgress() {}
    private fun showProgress() {}
    inner class BlogViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        fun bind(model: KepoPost, postKey: String) {
            itemView.apply {
                likeBtn(postKey)
                Glide.with(this).load(model.getImage()).into(post2_gambar)
                post2_judul.text = model.getJudul()
                post2_penjelasan.text = model.getPenjelasan()
                post2_username.text = model.getUsername()
                post2_provinsi.text = model.getProvinsi()
                post2_kategori.text = model.getKategori()


                BtnLike.setOnClickListener {
                    mProcessLike = true
                    mDatabaseLike?.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (mProcessLike) {
                                mProcessLike = if (dataSnapshot.child(postKey).hasChild(mAuth?.currentUser?.uid.toString())) {
                                    mDatabaseLike?.child(postKey)?.child(mAuth?.currentUser?.uid.toString())?.removeValue()
                                    false
                                } else {
                                    mDatabaseLike?.child(postKey)?.child(mAuth?.currentUser?.uid.toString())?.setValue("RandomValue")
                                    false
                                }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }

        }

        private var mDatabaseLike: DatabaseReference? = null
        private var mAuth: FirebaseAuth? = null
        private fun likeBtn(post_key: String) {
            itemView.apply {
                mDatabaseLike?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.child(post_key).hasChild(mAuth?.currentUser?.uid.toString())) {
                            BtnLike.setImageResource(R.drawable.ic_thumb_up_black_24dp)
                        } else {
                            BtnLike.setImageResource(R.drawable.ic_thumb_up_outline)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

        }

        init {
            mDatabaseLike = FirebaseDatabase.getInstance().reference.child("Likes")
            mAuth = FirebaseAuth.getInstance()
            mDatabaseLike?.keepSynced(true)
        }
    }
}