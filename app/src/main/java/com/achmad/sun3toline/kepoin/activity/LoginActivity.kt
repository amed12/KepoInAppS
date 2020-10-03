/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 8:56 PM
 *
 */

package com.achmad.sun3toline.kepoin.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.achmad.sun3toline.kepoin.R
import com.achmad.sun3toline.kepoin.utils.dialog.ProgressDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {
    private var mLoginEmailField: EditText? = null
    private var mLoginPasswordField: EditText? = null
    private var mLoginBtn: MaterialButton? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabaseUser: DatabaseReference? = null
    private var mRegister: MaterialButton? = null
    private var mProgress: ProgressDialog? = null
    private var passwTextInputLayout: TextInputLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        mProgress = ProgressDialog(this)
        mRegister = findViewById(R.id.signUpbtn)
        passwTextInputLayout = findViewById(R.id.password_text_input)
        mDatabaseUser = FirebaseDatabase.getInstance().reference.child("Users")
        mDatabaseUser?.keepSynced(true)
        mLoginPasswordField = findViewById(R.id.passfield)
        mLoginPasswordField?.setOnKeyListener { _, _, _ ->
            if (isPasswordValid(mLoginPasswordField?.text)) {
                passwTextInputLayout?.error = null //Clear the error
            }
            false
        }
        mLoginEmailField = findViewById(R.id.email1field)
        mLoginBtn = findViewById(R.id.lgnbtn)
        mRegister?.setOnClickListener {
            val register = Intent(this@LoginActivity, RegisterActivity::class.java)
            register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(register)
        }
        mLoginBtn?.setOnClickListener { checkLogin() }
    }

    private fun checkLogin() {
        val email = mLoginEmailField?.text.toString().trim { it <= ' ' }
        val password = mLoginPasswordField?.text.toString().trim { it <= ' ' }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && email.matches(emailPattern)) {
            mProgress?.setMessage("Checking Login...")
            mProgress?.show()
            mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mProgress?.dismiss()
                    checkUserExist()
                } else {
                    mProgress?.dismiss()
                    mLoginEmailField?.error = "isi dengan lengkap"
                    mLoginPasswordField?.error = "teliti lagi password anda"
                    Toast.makeText(this@LoginActivity, "Error Login", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            mLoginEmailField?.error = "isi dengan lengkap"
            mLoginPasswordField?.error = "teliti lagi password anda"
        }
    }

    private fun checkUserExist() {
        val userId = mAuth?.currentUser?.uid.toString()
        mDatabaseUser?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    val mainIntent = Intent(this@LoginActivity, KepoIn::class.java)
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(mainIntent)
                    finish()
                } else {
                    val setupIntent = Intent(this@LoginActivity, SetupActivity::class.java)
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(setupIntent)
                    finish()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun isPasswordValid(text: Editable?): Boolean {
        return text != null && text.length >= 8
    }
}