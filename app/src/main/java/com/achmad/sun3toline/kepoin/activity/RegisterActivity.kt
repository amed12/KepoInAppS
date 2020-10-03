/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 8:35 PM
 *
 */

package com.achmad.sun3toline.kepoin.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.achmad.sun3toline.kepoin.R
import com.achmad.sun3toline.kepoin.utils.dialog.ProgressDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private var mNameField: EditText? = null
    private var mEmailField: EditText? = null
    private var mPasswordField: EditText? = null
    private var mRegisterBtn: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var mProgress: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        mProgress = ProgressDialog(this)
        mNameField = findViewById(R.id.nameField)
        mEmailField = findViewById(R.id.emailField)
        mPasswordField = findViewById(R.id.passwordField)
        mRegisterBtn = findViewById(R.id.registerBtn)
        mRegisterBtn?.setOnClickListener { startRegister() }
    }

    private fun startRegister() {
        val name = mNameField?.text.toString().trim { it <= ' ' }
        val email = mEmailField?.text.toString().trim { it <= ' ' }
        val password = mPasswordField?.text.toString().trim { it <= ' ' }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        val passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})".toRegex()
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && email.matches(emailPattern) && password.matches(passwordPattern)) {
            mProgress?.setMessage("Signing Up")
            mProgress?.show()
            mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this@RegisterActivity, "User with this email already exist.", Toast.LENGTH_SHORT).show()
                    }
                    val userId = mAuth?.currentUser?.uid
                    val currentUserDb = mDatabase?.child(userId.toString())
                    currentUserDb?.child("name")?.setValue(name)
                    currentUserDb?.child("image")?.setValue("default")
                    mProgress?.dismiss()
                    val mainIntent = Intent(this@RegisterActivity, KepoIn::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(mainIntent)
                    finish()
                } else {
                    mProgress?.dismiss()
                    mEmailField?.error = "Email already exist or check email format"
                }
            }
        } else {
            mPasswordField?.error = "must contains one digit from 0-9,uppercase characters, length at least 8 characters and maximum of 20"
            mEmailField?.error = "please use valid email"
        }
    }
}