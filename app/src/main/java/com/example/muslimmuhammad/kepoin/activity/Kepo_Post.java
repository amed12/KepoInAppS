package com.example.muslimmuhammad.kepoin.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muslimmuhammad.kepoin.R;
import com.example.muslimmuhammad.kepoin.model.KepoPost;
import com.example.muslimmuhammad.kepoin.model.PostModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Kepo_Post extends AppCompatActivity {
    private RecyclerView mPostList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kepo__post);




        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabase.keepSynced(true);

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
        ) {
            @Override
            protected void populateViewHolder(Kepo_Post.BlogViewHolder viewHolder, KepoPost model, int position) {

                
                Picasso.with(Kepo_Post.this).load(model.getImage()).into(viewHolder.ImageBro);
                viewHolder.titlebro.setText(model.getJudul());
                viewHolder.descbro.setText(model.getPenjelasan());

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

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            descbro = (TextView) mView.findViewById(R.id.post_judul);
            ImageBro = (ImageView) mView.findViewById(R.id.post_gambar);
            titlebro = (TextView) mView.findViewById(R.id.post_penjelasan);
        }
    }
}
