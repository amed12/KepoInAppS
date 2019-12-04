package com.example.sun3toline.kepoin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sun3toline.kepoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by coldwarrior on 21/11/16.
 */

public class AddPostFragment extends AppCompatActivity {
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mDatabaseReference;
    private EditText txtdesc;
    private EditText txttitle;
    private ImageButton txtimage;
    private Button bSubmit;
    private Uri mImageUri = null;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;

    String province,kategori;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_user);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        txtdesc = findViewById(R.id.edt3);
        txttitle = findViewById(R.id.edt1);
        txtimage = findViewById(R.id.imageBro);
        bSubmit = findViewById(R.id.btn_input);
        mDatabaseReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
         province = getIntent().getStringExtra("data1");
         kategori = getIntent().getStringExtra("data2");

        mProgress = new ProgressDialog(this);


        txtimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });



    }







     @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
             mImageUri = data.getData();

             txtimage.setImageURI(mImageUri);



             bSubmit.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     mProgress.setMessage("Uploading to post...");


                     final String title_val = txttitle.getText().toString().trim();
                     final String desc_val = txtdesc.getText().toString().trim();


                     if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null) {
                         mProgress.show();
                         txtdesc.setError("data harus diisi");
                         txttitle.setError("data harus di isi");

                         StorageReference filepath = mDatabaseReference.child("image_post").child(mImageUri.getLastPathSegment());
                         filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                             @Override
                             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                 final Uri dowloadUrl = taskSnapshot.getDownloadUrl();
                                 final DatabaseReference newPost = mDatabase.push();

                                 mDatabaseUser.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         newPost.child("judul").setValue(title_val);
                                         newPost.child("penjelasan").setValue(desc_val);
                                         newPost.child("image").setValue(dowloadUrl.toString());
                                         newPost.child("provinsi").setValue(province);
                                         newPost.child("kategori").setValue(kategori);
                                         newPost.child("uid").setValue(mCurrentUser.getUid());
                                         newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful()){
                                                     startActivity(new Intent(AddPostFragment.this,Kepo_Post.class));


                                                 }

                                             }
                                         });


                                     }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 });
                                 mProgress.dismiss();


                             }
                         });


                     }
                 }
             });




         }
     }




     }



