/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 9:17 PM
 *
 */

package com.achmad.sun3toline.kepoin.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.achmad.sun3toline.kepoin.R
import com.achmad.sun3toline.kepoin.model.PostModel
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.skydoves.whatif.whatIf
import com.skydoves.whatif.whatIfNotNull

class Post : AppCompatActivity() {
    private var shrinkAnim: ScaleAnimation? = null

    //Getting reference to firebase database
    var database: FirebaseDatabase? = FirebaseDatabase.getInstance()
    private var progressbar: ProgressBar? = null
    private var refresh: SwipeRefreshLayout? = null
    private var province: String? = null
    private var kategori: String? = null
    private var mBlogList: RecyclerView? = null
    private var mLayoutmanager: LinearLayoutManager? = null
    private var fab: FloatingActionButton? = null
    private var tvNoMovies: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        tvNoMovies = findViewById(R.id.tv_no_movies)
        progressbar = findViewById(R.id.progressbar)
        progressbar?.visibility = View.GONE
        refresh = findViewById(R.id.refresh)
        mBlogList = findViewById(R.id.bloglist)
        shrinkAnim = ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        assignUIEvent()
        province = intent.getStringExtra("data1")
        kategori = intent.getStringExtra("data2")
        //        text1.setText(getIntent().getStringExtra("data1"));
//        text2.setText(getIntent().getStringExtra("data2"));
        fab = findViewById(R.id.fab)
        fab?.setOnClickListener {
            val intent = Intent(this@Post, AddPostFragment::class.java)
            intent.putExtra("data1", province)
            intent.putExtra("data2", kategori)
            startActivity(intent)

            //animation being used to make floating actionbar disappear
            shrinkAnim?.duration = 400
            fab?.animation = shrinkAnim
            shrinkAnim?.start()
            shrinkAnim?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    fab?.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
    }

    override fun onStart() {
        super.onStart()
        loadphoto()
    }

    private fun assignUIEvent() {
        refresh?.setOnRefreshListener { loadphoto() }
    }

    private fun loadphoto() {
        if (mBlogList != null) {
            mBlogList?.setHasFixedSize(true)
        }
        mLayoutmanager = LinearLayoutManager(this)
        mBlogList?.layoutManager = LinearLayoutManager(this)
        showProgress()
        val query = FirebaseDatabase.getInstance().reference.child(province.toString()).child(kategori.toString())
        val options: FirebaseRecyclerOptions<PostModel> = FirebaseRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel::class.java)
                .build()
        val adapter: FirebaseRecyclerAdapter<PostModel, BlogViewHolder> = object : FirebaseRecyclerAdapter<PostModel, BlogViewHolder>(
                options
        ) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
                return BlogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_post, parent, false))
            }

            override fun onBindViewHolder(viewHolder: BlogViewHolder, position: Int, model: PostModel) {
                viewHolder.description?.text = model.getPenjelasan()
                viewHolder.imageView.whatIfNotNull {
                    Glide.with(this@Post).load(model.getImage()).into(it)
                }
                viewHolder.title?.text = model.getNama()
            }
        }
        tvNoMovies?.visibility = View.GONE
        mBlogList?.adapter = adapter
        closeProgress()
    }

    //    public void onBackPressed() {
    //        super.onBackPressed();
    //        if (fab.getVisibility() == View.GONE)
    //            fab.setVisibility(View.VISIBLE);
    //    }
    private fun showProgress() {
        refresh?.isRefreshing.whatIf(
                whatIfNot = {
                    progressbar?.visibility = View.VISIBLE
                },
                whatIf = {
                    //
                }
        )
    }

    private fun closeProgress() {
        progressbar?.visibility = View.GONE
        refresh?.isRefreshing = false
    }

    class BlogViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        var title: TextView? = null
        var imageView: ImageView? = null
        var description: TextView? = null

        init {
            description = mView.findViewById(R.id.post_text)
            imageView = mView.findViewById(R.id.post_image)
            title = mView.findViewById(R.id.post_title)
        }
    }
}