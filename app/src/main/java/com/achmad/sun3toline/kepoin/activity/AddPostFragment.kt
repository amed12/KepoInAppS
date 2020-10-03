/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 8:20 PM
 *
 */

package com.achmad.sun3toline.kepoin.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.achmad.sun3toline.kepoin.R
import com.achmad.sun3toline.kepoin.utils.dialog.ProgressDialog
import com.achmad.sun3toline.kepoin.utils.extension.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.skydoves.whatif.whatIfNotNull
import kotlinx.android.synthetic.main.upload_user.*

/**
 * Created by coldwarrior on 21/11/16.
 */
class AddPostFragment : AppCompatActivity() {
    private lateinit var mDatabaseReference: StorageReference
    private var mImageUri: Uri? = null
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mProgress: ProgressDialog
    private lateinit var mAuth: FirebaseAuth
    private var mCurrentUser: FirebaseUser? = null
    private lateinit var mDatabaseUser: DatabaseReference
    var province: String? = null
    var kategori: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_user)
        mAuth = FirebaseAuth.getInstance()
        mCurrentUser = mAuth.currentUser
        mDatabaseReference = FirebaseStorage.getInstance().reference
        mDatabase = FirebaseDatabase.getInstance().reference.child("Post")
        mDatabaseUser = FirebaseDatabase.getInstance().reference.child("Users").child(mCurrentUser?.uid.toString())
        province = intent.getStringExtra("data1")
        kategori = intent.getStringExtra("data2")
        mProgress = ProgressDialog(this)
        imageBro.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_REQUEST)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data?.data
            mImageUri?.whatIfNotNull(
                    whatIf = { uri ->
                        imageBro.setImageURI(mImageUri)
                        btn_input.setOnClickListener {
                            mProgress.setMessage("Uploading to post...")
                            val titleVal = edt1.text.toString().trim { it <= ' ' }
                            val descVal = edt3.text.toString().trim { it <= ' ' }
                            if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal)) {
                                mProgress.show()
                                edt3.error = "data harus diisi"
                                edt1.error = "data harus di isi"
                                val filepath = mDatabaseReference.child("image_post").child(uri.lastPathSegment.toString())
                                filepath.putFile(uri).addOnSuccessListener {
                                    filepath.downloadUrl.addOnSuccessListener { taskSnapshot ->
                                        val dowloadUrl: Uri = taskSnapshot
                                        val newPost = mDatabase.push()
                                        mDatabaseUser.addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                newPost.child("judul").setValue(titleVal)
                                                newPost.child("penjelasan").setValue(descVal)
                                                newPost.child("image").setValue(dowloadUrl.toString())
                                                newPost.child("provinsi").setValue(province)
                                                newPost.child("kategori").setValue(kategori)
                                                newPost.child("uid").setValue(mCurrentUser?.uid.toString())
                                                newPost.child("username").setValue(dataSnapshot.child("name").value).addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        startActivity(Intent(this@AddPostFragment, PostActivity::class.java))
                                                    }
                                                }
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {}
                                        })
                                        mProgress.dismiss()
                                    }
                                }
                            }
                        }
                    },
                    whatIfNot = {
                        showToast(getString(R.string.error_take_image))
                    }
            )

        }
    }

    companion object {
        private const val GALLERY_REQUEST = 1
    }
}