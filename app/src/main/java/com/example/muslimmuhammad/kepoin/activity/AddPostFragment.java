package com.example.muslimmuhammad.kepoin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.muslimmuhammad.kepoin.R;
import com.example.muslimmuhammad.kepoin.model.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

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

    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_user);
        txtdesc = (EditText) findViewById(R.id.edt3);
        txttitle = (EditText) findViewById(R.id.edt1);
        txtimage = (ImageButton) findViewById(R.id.imageBro);
        bSubmit = (Button)findViewById(R.id.btn_input);
        mDatabaseReference = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this);


        txtimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

    }



    private void startPosting() {
        mProgress.setMessage("Uploading to post...");

        mProgress.show();
        String title_val = txttitle.getText().toString().trim();
        String desc_val = txtdesc.getText().toString().trim();
        //if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null) {
            txtdesc.setError("data harus diisi");
            txttitle.setError("data harus di isi");
            StorageReference filepath = mDatabaseReference.child("data_tarian").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri dowloadUrl = taskSnapshot.getDownloadUrl();
                    mProgress.dismiss();
                }
            });


    }



     @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
             mImageUri = data.getData();

             txtimage.setImageURI(mImageUri);
             mProgress.setMessage("Uploading to post...");

             mProgress.show();


             StorageReference filepath = mDatabaseReference.child("image_post").child(mImageUri.getLastPathSegment());
             filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Uri dowloadUrl = taskSnapshot.getDownloadUrl();
                     mProgress.dismiss();
                 }
             });


         }

     }


}
