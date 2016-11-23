package com.example.muslimmuhammad.kepoin.activity;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.muslimmuhammad.kepoin.R;

public class KepoIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton img1,img2,img3,img4,img5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kepo_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        img1 = (ImageButton)findViewById(R.id.imageButton2);
        img2 = (ImageButton)findViewById(R.id.imageButton3);
        img3 = (ImageButton)findViewById(R.id.imageButton4);
        img4 = (ImageButton)findViewById(R.id.imageButton5);
        img5 = (ImageButton)findViewById(R.id.imageButton6);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent();
                pindah = new Intent(KepoIn.this, Province.class );
                pindah.putExtra("nama","tari");
                startActivity(pindah);

            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent();
                pindah = new Intent(KepoIn.this, Province.class );
                pindah.putExtra("nama","pakaianadat");
                startActivity(pindah);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent();
                pindah = new Intent(KepoIn.this, Province.class );
                pindah.putExtra("nama","rumahadat");
                startActivity(pindah);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent();
                pindah = new Intent(KepoIn.this, Province.class );
                pindah.putExtra("nama","flora");
                startActivity(pindah);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent();
                pindah = new Intent(KepoIn.this, Province.class );
                pindah.putExtra("nama","fauna");
                startActivity(pindah);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Toast.makeText(this,"Halaman Utama KepoIn",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"Postingan Terpopuler",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Postingan Anda", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_about){
            Toast.makeText(this, "Informasi Tentang Pengembang", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"Berbagi KepoIn Disosmed",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {

            Toast.makeText(this,"Keluar Dari KepoIn",Toast.LENGTH_SHORT).show();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
