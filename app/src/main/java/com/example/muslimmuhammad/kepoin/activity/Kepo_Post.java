package com.example.muslimmuhammad.kepoin.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muslimmuhammad.kepoin.R;
import com.example.muslimmuhammad.kepoin.model.KepoPost;
import com.example.muslimmuhammad.kepoin.model.PostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Kepo_Post extends AppCompatActivity {
    private RecyclerView mPostList;
    private DatabaseReference mDatabase;
    private boolean mProssesLike = false;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseCurrentUser;

    private FirebaseAuth mAuth;
    private Query mQueryCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kepo__post);
        mAuth = FirebaseAuth.getInstance();


        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);
        //mDatabaseCurrentUser.keepSynced(true);
        String currentUserID = mAuth.getCurrentUser().getUid();

        mQueryCurrentUser = mDatabaseCurrentUser.orderByChild("uid").equalTo(currentUserID);
        mPostList = (RecyclerView) findViewById(R.id.post_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(layoutManager);



    }

    @Override
    protected void onStart() {
        super.onStart();
        loadphoto();
    }
    private void loadphoto(){

        showProgress();
        FirebaseRecyclerAdapter<KepoPost,Kepo_Post.BlogViewHolder> adapter = new FirebaseRecyclerAdapter<KepoPost, Kepo_Post.BlogViewHolder>(
                KepoPost.class,
                R.layout.item_list,
                Kepo_Post.BlogViewHolder.class,
                mDatabase
                //mQueryCurrentUser
        ) {
            @Override
            protected void populateViewHolder(final Kepo_Post.BlogViewHolder viewHolder, final KepoPost model, int position) {

                final String post_key = getRef(position).getKey() ;
                //Picasso.with(Kepo_Post.this).load(model.getImage()).into(viewHolder.ImageBro);
                Picasso.with(Kepo_Post.this).load(model.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(viewHolder.ImageBro, new Callback() {
                    @Override
                    public void onSuccess() {


                    }

                    @Override
                    public void onError() {
                        Picasso.with(Kepo_Post.this).load(model.getImage()).into(viewHolder.ImageBro);
                    }
                });
                viewHolder.titlebro.setText(model.getJudul());
                viewHolder.descbro.setText(model.getPenjelasan());
                viewHolder.Username.setText(model.getUsername());
                viewHolder.Provinsi.setText(model.getProvinsi());
                viewHolder.Kategori.setText(model.getKategori());
                viewHolder.setmLikeBtn(post_key);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(Kepo_Post.this,post_key,Toast.LENGTH_LONG).show();
                        Intent singleBlogIntent = new Intent(Kepo_Post.this,BlogSingleActivity.class);
                        singleBlogIntent.putExtra("blog_id",post_key);
                        startActivity(singleBlogIntent);
                    }
                });


                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProssesLike = true;

                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(mProssesLike){
                                        if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mProssesLike = false;
                                        }else {
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                            mProssesLike = false;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                    }
                });

            }
        };


        mPostList.setAdapter(adapter);
        closeProgress();
    }

    private void closeProgress() {
    }

    private void showProgress() {
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView titlebro;
        TextView descbro;
        ImageView ImageBro;
        TextView Username;
        TextView Kategori;
        TextView Provinsi;
        ImageButton mLikeBtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            final Context context = null;

            mLikeBtn = (ImageButton)mView.findViewById(R.id.BtnLike);
            mDatabaseLike =FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);
            ImageBro = (ImageView) mView.findViewById(R.id.post2_gambar);
            titlebro = (TextView)mView.findViewById(R.id.post2_judul);
            Kategori = (TextView)mView.findViewById(R.id.post2_kategori);
            descbro = (TextView) mView.findViewById(R.id.post2_judul);
            Provinsi = (TextView)mView.findViewById(R.id.post2_provinsi);
            Username = (TextView)mView.findViewById(R.id.post2_username);

            Username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(Context contex,"Halo",Toast.LENGTH_LONG).show();
                }
            });


        }
        public void  setmLikeBtn (final String post_key){
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){
                        mLikeBtn.setImageResource(R.drawable.thumbsred);
                    }else {
                        mLikeBtn.setImageResource(R.drawable.thumbwhite);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
