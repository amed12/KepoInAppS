package com.example.muslimmuhammad.kepoin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.muslimmuhammad.kepoin.R;
import com.example.muslimmuhammad.kepoin.model.KepoPost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Setup_activity extends AppCompatActivity {
    private ImageButton mSetupImageBtn;
    private EditText mNameField;
    private Button mSubmitBtn;
    private Uri mImageUri = null;
    private DatabaseReference mDatabaseUser;
    private static final int GALLERY_REQUEST = 1;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage ;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_activity);
        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profil_image");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mSubmitBtn = (Button)findViewById(R.id.btn_input);
        mNameField = (EditText) findViewById(R.id.edt1);
        mSetupImageBtn = (ImageButton)findViewById(R.id.imageBro);
        mProgress = new ProgressDialog(this);

        mSetupImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });


    }

    private void startSetupAccount() {

        final String name = mNameField.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();


        if(!TextUtils.isEmpty(name) && mImageUri != null){
            mProgress.setMessage("Finishing setup...");
            mProgress.show();
            StorageReference filePath = mStorageImage.child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    mDatabaseUser.child(user_id).child("name").setValue(name);
                    mDatabaseUser.child(user_id).child("image").setValue(downloadUri);
                    mProgress.dismiss();
                    Intent mainIntent = new Intent(Setup_activity.this,KepoIn.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadphoto();

    }
    private void loadphoto(){
        mDatabaseUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.child("name").getValue();

                String post_image = (String) dataSnapshot.child("image").getValue();


                mNameField.setText(username);

                //Picasso.with(Setup_activity.this).load(post_image).placeholder(R.drawable.common_full_open_on_phone).into(mSetupImageBtn);

//                Picasso.with(Setup_activity.this).load(post_image).into(mSetupImageBtn);
//                mSetupImageBtn.setImageResource();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK ){
            mImageUri = data.getData();
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setActivityTitle("Crop your image")
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setMaxCropResultSize(1000,1000)
                    .setMinCropResultSize(100,100)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                mSetupImageBtn.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
