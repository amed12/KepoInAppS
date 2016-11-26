package com.example.muslimmuhammad.kepoin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muslimmuhammad.kepoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    private Button mRegister;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog((this));
        mRegister = (Button)findViewById(R.id.signUpbtn);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);
        mLoginPasswordField = (EditText) findViewById(R.id.passfield);
        mLoginEmailField = (EditText)findViewById(R.id.email1field);
        mLoginBtn = (Button)findViewById(R.id.lgnbtn);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginActivity.this,RegisterActivity.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(register);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 checkLogin();
            }
        });


    }

    private void checkLogin() {
        final String email = mLoginEmailField.getText().toString().trim();
        String password = mLoginPasswordField.getText().toString().trim();

        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            mProgress.setMessage("Checking Login...");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        mProgress.dismiss();
                        checkUserExist();

                    }else {
                        mProgress.dismiss();
                        mLoginEmailField.setError("isi dengan lengkap");
                        mLoginPasswordField.setError("teliti lagi password anda");
                        Toast.makeText(LoginActivity.this,"Error Login",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkUserExist() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    Intent mainIntent = new Intent(LoginActivity.this,KepoIn.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }else {
                    Intent setupIntent = new Intent(LoginActivity.this,Setup_activity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
