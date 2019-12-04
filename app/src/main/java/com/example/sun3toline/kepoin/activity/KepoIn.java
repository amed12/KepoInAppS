package com.example.sun3toline.kepoin.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sun3toline.kepoin.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class KepoIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton img1, img2, img3, img4, img5;
    TextView mProfilText;
    CircleImageView img13;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kepo_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(KepoIn.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    finish();
                } else {
                    checkUserExist();
                    loadprofil();
                }
            }
        };


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        img1 = findViewById(R.id.imageButton2);
        img2 = findViewById(R.id.imageButton3);
        img3 = findViewById(R.id.imageButton4);
        img4 = findViewById(R.id.imageButton5);
        img5 = findViewById(R.id.imageButton6);
        img13 = findViewById(R.id.imageView32);
        mProfilText = findViewById(R.id.profil_text);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah;
                pindah = new Intent(KepoIn.this, Province.class);
                pindah.putExtra("nama", "tari");
                startActivity(pindah);

            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent();
                pindah = new Intent(KepoIn.this, Province.class);
                pindah.putExtra("nama", "pakaianadat");
                startActivity(pindah);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah;
                pindah = new Intent(KepoIn.this, Province.class);
                pindah.putExtra("nama", "rumahadat");
                startActivity(pindah);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah;
                pindah = new Intent(KepoIn.this, Province.class);
                pindah.putExtra("nama", "flora");
                startActivity(pindah);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah;
                pindah = new Intent(KepoIn.this, Province.class);
                pindah.putExtra("nama", "fauna");
                startActivity(pindah);
            }
        });
//        checkUserExist();
//        loadprofil();
    }


    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(user_id)) {
                    Intent setupIntent = new Intent(KepoIn.this, Setup_activity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadprofil() {
        mDatabaseUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("name").getValue().toString();
//                TextUtils.isEmpty(username);
                mProfilText = findViewById(R.id.profil_text);
                img13 = findViewById(R.id.imageView32);
                mProfilText.setText("Hello " + username);

                //String post_image = (String) dataSnapshot.child("image").getValue();

                String post_imagew = dataSnapshot.child("image").getValue().toString();
                Picasso.with(KepoIn.this).load(post_imagew).placeholder(R.drawable.logo).into(img13);


//                Picasso.with(Setup_activity.this).load(post_image).placeholder(R.drawable.common_full_open_on_phone).into(mSetupImageBtn);

//                Picasso.with(Setup_activity.this).load(post_image).into(mSetupImageBtn);
//                mSetupImageBtn.setImageResource();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

//        loadprofil();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kepo_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(this, "Keluar dari Kepo Indonesia", Toast.LENGTH_SHORT).show();
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this, "Halaman Utama KepoIn", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Intent pindahKepost = new Intent(KepoIn.this, Kepo_Post.class);
            startActivity(pindahKepost);
            Toast.makeText(this, "Postingan Anda", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_login) {
            Intent setting = new Intent(KepoIn.this, Setup_activity.class);
            startActivity(setting);
            Toast.makeText(this, "Profile Kepo Mu", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Tentang Pengembang", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
