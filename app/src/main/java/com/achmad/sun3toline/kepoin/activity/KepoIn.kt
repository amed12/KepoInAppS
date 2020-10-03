/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 9:05 PM
 *
 */

package com.achmad.sun3toline.kepoin.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.achmad.sun3toline.kepoin.R
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.content_kepo_in.*
import kotlinx.android.synthetic.main.nav_header_kepo_in.*

open class KepoIn : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: AuthStateListener
    private lateinit var mDatabaseUser: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kepo_in)
        val toolbar = findViewById<Toolbar?>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mDatabaseUser = FirebaseDatabase.getInstance().reference.child("Users")
        mDatabaseUser.keepSynced(true)
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                val loginIntent = Intent(this@KepoIn, LoginActivity::class.java)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)
                finish()
            } else {
                checkUserExist()
                loadprofil()
            }
        }
        val drawer = findViewById<DrawerLayout?>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        //        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView?>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        imageButton2.setOnClickListener {
            val pindah = Intent(this@KepoIn, Province::class.java)
            pindah.putExtra("nama", "tari")
            startActivity(pindah)
        }
        imageButton3.setOnClickListener {
            val pindah = Intent(this@KepoIn, Province::class.java)
            pindah.putExtra("nama", "pakaianadat")
            startActivity(pindah)
        }
        imageButton4.setOnClickListener {
            val pindah = Intent(this@KepoIn, Province::class.java)
            pindah.putExtra("nama", "rumahadat")
            startActivity(pindah)
        }
        imageButton5.setOnClickListener {
            val pindah = Intent(this@KepoIn, Province::class.java)
            pindah.putExtra("nama", "flora")
            startActivity(pindah)
        }
        imageButton6.setOnClickListener {
            val pindah = Intent(this@KepoIn, Province::class.java)
            pindah.putExtra("nama", "fauna")
            startActivity(pindah)
        }
        //        checkUserExist();
//        loadprofil();
    }

    private fun checkUserExist() {
        val userId = mAuth.currentUser?.uid ?: "0"
        mDatabaseUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.hasChild(userId)) {
                    val setupIntent = Intent(this@KepoIn, SetupActivity::class.java)
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(setupIntent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun loadprofil() {
        mDatabaseUser.child(mAuth.currentUser?.uid.toString()).addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.child("name").value != null && dataSnapshot.child("image").value != null) {
                    val username = dataSnapshot.child("name").value.toString()
                    profil_text.text = "Hello $username"
                    val postImagew = dataSnapshot.child("image").value.toString()
                    Glide.with(applicationContext).load(postImagew).placeholder(R.drawable.logo).into(imageView32)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onStart() {
        super.onStart()

//        loadprofil();
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout?>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.kepo_in, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_logout) {
            Toast.makeText(this, "Keluar dari Kepo Indonesia", Toast.LENGTH_SHORT).show()
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        mAuth.signOut()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                Toast.makeText(this, "Halaman Utama KepoIn", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_manage -> {
                val pindahKepost = Intent(this@KepoIn, PostActivity::class.java)
                startActivity(pindahKepost)
                Toast.makeText(this, "Postingan Anda", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_login -> {
                val setting = Intent(this@KepoIn, SetupActivity::class.java)
                startActivity(setting)
                Toast.makeText(this, "Profile Kepo Mu", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_about -> {
                Toast.makeText(this, "Tentang Pengembang", Toast.LENGTH_SHORT).show()
            }
        }
        val drawer = findViewById<DrawerLayout?>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}