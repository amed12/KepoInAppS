package com.example.muslimmuhammad.kepoin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmuhammad.kepoin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;

public class BlogSingleActivity extends AppCompatActivity {
    private String mPost_key = null;
    private DatabaseReference mDatabase;
    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle;
    private TextView mBlogSingleDesc;
    private TextView mBlogSingleProvinsi;
    private TextView mBlogSingleKategori;
    private TextView mBlogSingleUsername;
    private Button mSingleRemoveBtn;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);
        mPost_key = getIntent().getExtras().getString("blog_id");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        //Toast.makeText(BlogSingleActivity.this,mPost_key,Toast.LENGTH_LONG).show();
        mAuth = FirebaseAuth.getInstance();
        mBlogSingleDesc = (TextView)findViewById(R.id.single_desc);
        mBlogSingleImage = (ImageView)findViewById(R.id.single_image);
        mBlogSingleTitle = (TextView)findViewById(R.id.single_title);
        mBlogSingleKategori = (TextView)findViewById(R.id.single_kategori);
        mBlogSingleProvinsi = (TextView)findViewById(R.id.single_provinsi);
        mSingleRemoveBtn = (Button)findViewById(R.id.singleRemovebtn);
        mBlogSingleUsername = (TextView)findViewById(R.id.single_username);
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("judul").getValue();
                String post_desc = (String) dataSnapshot.child("penjelasan").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid = (String)dataSnapshot.child("uid").getValue();
                String post_username = (String)dataSnapshot.child("username").getValue();
                String post_kategori = (String)dataSnapshot.child("kategori").getValue();
                String post_provinsi = (String)dataSnapshot.child("provinsi").getValue();

                mBlogSingleTitle.setText(post_title);
                mBlogSingleDesc.setText(post_desc);
                Picasso.with(BlogSingleActivity.this).load(post_image).into(mBlogSingleImage);
                mBlogSingleKategori.setText(post_kategori);
                mBlogSingleProvinsi.setText(post_provinsi);
                mBlogSingleUsername.setText(post_username);
                if (mAuth.getCurrentUser().getUid().equals(post_uid)){
                    mSingleRemoveBtn.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mPost_key).removeValue();
                Intent mainIntent = new Intent(BlogSingleActivity.this,Kepo_Post.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }
}
