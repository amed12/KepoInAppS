package com.example.muslimmuhammad.kepoin.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.muslimmuhammad.kepoin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.example.muslimmuhammad.kepoin.model.PostModel;

public class Post extends AppCompatActivity {
    private MenuItem btn;
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private LinearLayoutManager mLayoutmanager;
    ScaleAnimation shrinkAnim;
    private FloatingActionButton fab;
    private TextView tvNoMovies;
    //Getting reference to firebase database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();
    ProgressBar progressbar;
    SwipeRefreshLayout refresh;
    String province,kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        tvNoMovies = (TextView)findViewById(R.id.tv_no_movies);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        mBlogList = (RecyclerView)findViewById(R.id.bloglist);
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);

        assignUIEvent();

        TextView text1 = (TextView)findViewById(R.id.ambil1);
        TextView text2 = (TextView)findViewById(R.id.ambil2);
        province = getIntent().getStringExtra("data1");
        kategori = getIntent().getStringExtra("data2");
//        text1.setText(getIntent().getStringExtra("data1"));
//        text2.setText(getIntent().getStringExtra("data2"));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Post.this,AddPostFragment.class);
                startActivity(intent);

                //animation being used to make floating actionbar disappear
                shrinkAnim.setDuration(400);
                fab.setAnimation(shrinkAnim);
                shrinkAnim.start();
                shrinkAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //changing floating actionbar visibility to gone on animation end
                        fab.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {


                    }
                });

            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        loadphoto();
    }

    private void assignUIEvent() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadphoto();
            }
        });
    }
    private void loadphoto(){

        if(mBlogList != null){
            mBlogList.setHasFixedSize(true);
        }

        mLayoutmanager = new LinearLayoutManager(this);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));


        showProgress();
        FirebaseRecyclerAdapter<PostModel,BlogViewHolder> adapter = new FirebaseRecyclerAdapter<PostModel, BlogViewHolder>(
                PostModel.class,
                R.layout.list_post,
                BlogViewHolder.class,
                mDatabaseReference.child(province).child(kategori).getRef()
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, PostModel model, int position) {

                viewHolder.descbro.setText(model.getPenjelasan());
                Picasso.with(Post.this).load(model.getImage()).into(viewHolder.ImageBro);
                viewHolder.titlebro.setText(model.getNama());

            }
        };

        tvNoMovies.setVisibility(View.GONE);
        mBlogList.setAdapter(adapter);
        closeProgress();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView titlebro;
        TextView descbro;
        ImageView ImageBro;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            descbro = (TextView)mView.findViewById(R.id.post_text);
            ImageBro = (ImageView)mView.findViewById(R.id.post_image);
            titlebro = (TextView)mView.findViewById(R.id.post_title);
        }






    }
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (fab.getVisibility() == View.GONE)
//            fab.setVisibility(View.VISIBLE);
//    }
    private void showProgress(){
        if (!refresh.isRefreshing()){
            progressbar.setVisibility(View.VISIBLE);
        }
    }

    private void closeProgress(){
        progressbar.setVisibility(View.GONE);
        refresh.setRefreshing(false);
    }
}
