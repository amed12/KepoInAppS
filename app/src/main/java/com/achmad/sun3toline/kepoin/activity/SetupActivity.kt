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
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.achmad.sun3toline.kepoin.R
import com.achmad.sun3toline.kepoin.utils.dialog.ProgressDialog
import com.achmad.sun3toline.kepoin.utils.extension.showToast
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.skydoves.whatif.whatIfNotNull
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class SetupActivity : AppCompatActivity() {
    private var mSetupImageBtn: ImageButton? = null
    private var mNameField: EditText? = null
    private var mSubmitBtn: MaterialButton? = null
    private var mImageUri: Uri? = null
    private var mDatabaseUser: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var mStorageImage: StorageReference? = null
    private var mProgress: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_activity)
        mAuth = FirebaseAuth.getInstance()
        mStorageImage = FirebaseStorage.getInstance().reference.child("Profil_image")
        mDatabaseUser = FirebaseDatabase.getInstance().reference.child("Users")
        mSubmitBtn = findViewById(R.id.btn_input)
        mNameField = findViewById(R.id.edt1)
        mSetupImageBtn = findViewById(R.id.imageBro)
        mProgress = ProgressDialog(this)
        mSetupImageBtn?.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_REQUEST)
        }
        mSubmitBtn?.setOnClickListener { startSetupAccount() }
    }

    private fun startSetupAccount() {
        val name = mNameField?.text.toString().trim { it <= ' ' }
        val userId = mAuth?.currentUser?.uid.toString()
        mImageUri.whatIfNotNull {
            mProgress?.setMessage("Finishing setup...")
            mProgress?.show()
            val filePath = mStorageImage?.child(it.lastPathSegment.toString())
            filePath?.putFile(it)?.addOnSuccessListener {
                filePath.downloadUrl.addOnSuccessListener { taskSnapshot ->
                    val downloadUri: Uri = taskSnapshot
                    mDatabaseUser?.child(userId)?.child("name")?.setValue(name)
                    mDatabaseUser?.child(userId)?.child("image")
                            ?.setValue(downloadUri)
                    mProgress?.dismiss()
                    val mainIntent = Intent(this@SetupActivity, KepoIn::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(mainIntent)
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadPhoto()
    }

    private fun loadPhoto() {
        mDatabaseUser?.child(mAuth?.currentUser?.uid.toString())?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.child("name").value as String?
                val postImage = dataSnapshot.child("image").value as String?
                mNameField?.setText(username)
                mSetupImageBtn?.let { Glide.with(this@SetupActivity).load(postImage).placeholder(R.drawable.common_full_open_on_phone).into(it) }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            data?.data.whatIfNotNull(
                    whatIf = {
                        mImageUri = it
                        CropImage.activity(it)
                                .setActivityTitle("Crop your image")
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .setMaxCropResultSize(1000, 1000)
                                .setMinCropResultSize(100, 100)
                                .start(this)
                    },
                    whatIfNot = {
                        showToast(R.string.error_take_image)
                    }
            )
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                mImageUri = result.uri
                mSetupImageBtn?.setImageURI(mImageUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showToast(result.error.toString())
            }
        }
    }

    companion object {
        private const val GALLERY_REQUEST = 1
    }
}